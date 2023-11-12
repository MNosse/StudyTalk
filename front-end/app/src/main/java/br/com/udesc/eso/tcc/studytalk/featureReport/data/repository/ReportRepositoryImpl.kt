package br.com.udesc.eso.tcc.studytalk.featureReport.data.repository

import android.content.Context
import br.com.udesc.eso.tcc.studytalk.core.data.repository.BaseRepositoryImpl
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.dataSource.LocalInstitutionDataSource
import br.com.udesc.eso.tcc.studytalk.featureReport.data.converter.convertToModel
import br.com.udesc.eso.tcc.studytalk.featureReport.data.converter.convertToRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureReport.data.dataSource.LocalReportDataSource
import br.com.udesc.eso.tcc.studytalk.featureReport.data.dataSource.RemoteReportDataSource
import br.com.udesc.eso.tcc.studytalk.featureReport.data.entity.ReportRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureReport.data.model.ReportApiModel
import br.com.udesc.eso.tcc.studytalk.featureReport.data.request.CreateReportRequest
import br.com.udesc.eso.tcc.studytalk.featureReport.data.response.GetAllReportsResponse
import br.com.udesc.eso.tcc.studytalk.featureReport.domain.model.Report
import br.com.udesc.eso.tcc.studytalk.featureReport.domain.repository.ReportRepository
import retrofit2.Response
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val context: Context,
    private val localInstitutionDataSource: LocalInstitutionDataSource,
    private val localReportDataSource: LocalReportDataSource,
    private val remoteReportDataSource: RemoteReportDataSource
) : ReportRepository, BaseRepositoryImpl() {

    override suspend fun approve(id: Long, approverUid: String): Result<Unit> {
        return try {
            val response = remoteReportDataSource.approve(
                id = id,
                approverUid = approverUid
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun create(
        requestingParticipantUid: String,
        postableId: Long,
        postableType: String,
        description: String
    ): Result<Unit> {
        return try {
            val response = remoteReportDataSource.create(
                request = CreateReportRequest(
                    requestingParticipantUid = requestingParticipantUid,
                    postableId = postableId,
                    postableType = postableType,
                    description = description
                )
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    localReportDataSource.create(
                        reportRoomEntity = convertToRoomEntity(
                            it
                        )
                    )
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllByInstitution(
        id: Long,
        requestingParticipantUid: String
    ): Result<MutableList<Report>> {
        return try {
            if (isOnline(context)) {
                getAllRemote(
                    remoteReportDataSource.getAllByInstitution(
                        id = id,
                        requestingParticipantUid = requestingParticipantUid
                    )
                )
            } else {
                getAllLocal(
                    localReportDataSource.getAllByInstitution(
                        id = id,
                        requestingParticipantUid = requestingParticipantUid
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getById(
        id: Long,
        requestingParticipantUid: String
    ): Result<Report?> {
        return try {
            if (isOnline(context)) {
                getRemote(
                    remoteReportDataSource.getById(
                        id = id,
                        requestingParticipantUid = requestingParticipantUid
                    )
                )
            } else {
                getLocal(
                    localReportDataSource.getById(
                        id = id,
                        requestingParticipantUid = requestingParticipantUid
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun reprove(id: Long, reproverUid: String): Result<Unit> {
        return try {
            val response = remoteReportDataSource.reprove(
                id = id,
                reproverUid = reproverUid
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getAllLocal(response: MutableList<ReportRoomEntity>): Result<MutableList<Report>> {
        val reports = mutableListOf<Report>()
        for (report in response) {
            val institution =
                localInstitutionDataSource.getByIdRelationship(report.institutionId)!!
            reports.add(
                convertToModel(report, institution)
            )
        }
        return Result.success(reports)
    }

    private fun getAllRemote(response: Response<GetAllReportsResponse>): Result<MutableList<Report>> {
        return if (response.isSuccessful) {
            val reports = mutableListOf<Report>()
            response.body()?.let {
                for (report in it.reports) {
                    reports.add(convertToModel(report))
                }
            }
            Result.success(reports)
        } else {
            Result.failure(Exception(response.errorBody()?.string()))
        }
    }

    private suspend fun getLocal(response: ReportRoomEntity?): Result<Report?> {
        return response?.let {
            val institution = localInstitutionDataSource.getByIdRelationship(it.institutionId)!!
            Result.success(
                convertToModel(it, institution)
            )
        } ?: Result.failure(Exception("A denúncia não foi encontrada."))
    }

    private fun getRemote(response: Response<ReportApiModel>): Result<Report?> {
        return if (response.isSuccessful) {
            response.body()!!.let {
                Result.success(convertToModel(it))
            }
        } else {
            Result.failure(Exception(response.errorBody()?.string()))
        }
    }

}