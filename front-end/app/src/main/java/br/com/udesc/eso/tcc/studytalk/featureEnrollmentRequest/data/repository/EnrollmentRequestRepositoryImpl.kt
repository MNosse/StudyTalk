package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.repository

import br.com.udesc.eso.tcc.studytalk.core.data.repository.BaseRepositoryImpl
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.converter.convertToModel
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.dataSource.LocalEnrollmentRequestDataSource
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.dataSource.RemoteEnrollmentRequestDataSource
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.entity.EnrollmentRequestRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.model.EnrollmentRequestApiModel
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.response.GetAllEnrollmentRequestiesResponse
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.model.EnrollmentRequest
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.repository.EnrollmentRequestRepository
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.dataSource.LocalInstitutionDataSource
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.dataSource.LocalParticipantDataSource
import retrofit2.Response
import javax.inject.Inject

class EnrollmentRequestRepositoryImpl @Inject constructor(
    private val localEnrollmentRequestDataSource: LocalEnrollmentRequestDataSource,
    private val localInstitutionDataSource: LocalInstitutionDataSource,
    private val localParticipantDataSource: LocalParticipantDataSource,
    private val remoteEnrollmentRequestDataSource: RemoteEnrollmentRequestDataSource
) : EnrollmentRequestRepository, BaseRepositoryImpl() {

    override suspend fun approve(id: Long, approverUid: String): Result<Unit> {
        return try {
            val response = remoteEnrollmentRequestDataSource.approve(
                id = id,
                approverUid = approverUid
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("$response.errorBody()"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllByInstitution(
        id: Long,
        requestingParticipantUid: String
    ): Result<MutableList<EnrollmentRequest>> {
        return try {
            if (isOnline()) {
                getAllRemote(
                    remoteEnrollmentRequestDataSource.getAllByInstitution(
                        id = id,
                        requestingParticipantUid = requestingParticipantUid
                    )
                )
            } else {
                getAllLocal(
                    localEnrollmentRequestDataSource.getAllByInstitution(
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
    ): Result<EnrollmentRequest?> {
        return try {
            if (isOnline()) {
                getRemote(
                    remoteEnrollmentRequestDataSource.getById(
                        id = id,
                        requestingParticipantUid = requestingParticipantUid
                    )
                )
            } else {
                getLocal(
                    localEnrollmentRequestDataSource.getById(
                        id = id,
                        requestingParticipantUid = requestingParticipantUid
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getByParticipantId(
        id: Long,
        requestingParticipantUid: String
    ): Result<EnrollmentRequest?> {
        return try {
            if (isOnline()) {
                getRemote(
                    remoteEnrollmentRequestDataSource.getByParticipant(
                        id = id,
                        requestingParticipantUid = requestingParticipantUid
                    )
                )
            } else {
                getLocal(
                    localEnrollmentRequestDataSource.getByParticipant(
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
            val response = remoteEnrollmentRequestDataSource.reprove(
                id = id,
                reproverUid = reproverUid
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("$response.errorBody()"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getAllLocal(response: MutableList<EnrollmentRequestRoomEntity>): Result<MutableList<EnrollmentRequest>> {
        val enrollmentRequesties = mutableListOf<EnrollmentRequest>()
        for (enrollmentRequest in response) {
            val institution =
                localInstitutionDataSource.getByIdRelationship(enrollmentRequest.institutionId)!!
            val participant =
                localParticipantDataSource.getByIdRelationship(enrollmentRequest.participantId)!!
            enrollmentRequesties.add(
                convertToModel(enrollmentRequest, institution, participant)
            )
        }
        return Result.success(enrollmentRequesties)
    }

    private fun getAllRemote(response: Response<GetAllEnrollmentRequestiesResponse>): Result<MutableList<EnrollmentRequest>> {
        return if (response.isSuccessful) {
            val enrollmentRequesties = mutableListOf<EnrollmentRequest>()
            response.body()?.let {
                for (enrollmentRequest in it.enrollmentRequesties) {
                    enrollmentRequesties.add(
                        convertToModel(enrollmentRequest)
                    )
                }
            }
            Result.success(enrollmentRequesties)
        } else {
            Result.failure(Exception("$response.errorBody()"))
        }
    }

    private suspend fun getLocal(response: EnrollmentRequestRoomEntity?): Result<EnrollmentRequest?> {
        return response?.let {
            val institution = localInstitutionDataSource.getByIdRelationship(it.institutionId)!!
            val participant = localParticipantDataSource.getByIdRelationship(it.participantId)!!
            Result.success(
                convertToModel(it, institution, participant)
            )
        } ?: Result.failure(Exception("A solicitação de cadastro não foi encontrada."))
    }

    private fun getRemote(response: Response<EnrollmentRequestApiModel>): Result<EnrollmentRequest?> {
        return if (response.isSuccessful) {
            response.body()!!.let {
                Result.success(
                    convertToModel(it)
                )
            }
        } else {
            Result.failure(Exception("$response.errorBody()"))
        }
    }

}