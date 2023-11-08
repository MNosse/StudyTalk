package br.com.udesc.eso.tcc.studytalk.featureInstitution.data.repository

import br.com.udesc.eso.tcc.studytalk.core.data.repository.BaseRepositoryImpl
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.converter.convertToModel
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.dataSource.LocalInstitutionDataSource
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.dataSource.RemoteInstitutionDataSource
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.entity.InstitutionRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.model.InstitutionApiModel
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.request.CreateInstitutionRequest
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.request.UpdateInstitutionRequest
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.response.GetAllInstitutionsResponse
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.model.Institution
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.repository.InstitutionRepository
import retrofit2.Response
import javax.inject.Inject

class InstitutionRepositoryImpl @Inject constructor(
    private val localInstitutionDataSource: LocalInstitutionDataSource,
    private val remoteInstitutionDataSource: RemoteInstitutionDataSource
) : InstitutionRepository, BaseRepositoryImpl() {

    override suspend fun create(administratorUid: String, name: String): Result<Unit> {
        return try {
            val response = remoteInstitutionDataSource.create(
                request = CreateInstitutionRequest(
                    administratorUid = administratorUid,
                    name = name
                )
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    localInstitutionDataSource.create(
                        institutionRoomEntity = br.com.udesc.eso.tcc.studytalk.featureInstitution.data.converter.convertToRoomEntity(
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

    override suspend fun delete(id: Long, administratorUid: String): Result<Unit> {
        return try {
            val response = remoteInstitutionDataSource.delete(
                id = id,
                administratorUid = administratorUid
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

    override suspend fun getAll(administratorUid: String): Result<MutableList<Institution>> {
        return try {
            if (isOnline()) {
                getAllRemote(remoteInstitutionDataSource.getAll(administratorUid = administratorUid))
            } else {
                getAllLocal(localInstitutionDataSource.getAll(administratorUid = administratorUid))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getById(
        id: Long,
        requestingUid: String,
        isAdministrator: Boolean
    ): Result<Institution?> {
        return try {
            if (isOnline()) {
                getRemote(
                    remoteInstitutionDataSource.getById(
                        id = id,
                        requestingUid = requestingUid,
                        isAdministrator = isAdministrator
                    )
                )
            } else {
                getLocal(
                    localInstitutionDataSource.getById(
                        id = id,
                        requestingUid = requestingUid,
                        isAdministrator = isAdministrator
                    )
                )

            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getByRegistrationCode(
        registrationCode: String,
        requestingUid: String,
        isAdministrator: Boolean
    ): Result<Institution?> {
        return try {
            if (isOnline()) {
                getRemote(
                    remoteInstitutionDataSource.getByRegistrationCode(
                        registrationCode = registrationCode,
                        requestingUid = requestingUid,
                        isAdministrator = isAdministrator
                    )
                )
            } else {
                getLocal(
                    localInstitutionDataSource.getByRegistrationCode(
                        registrationCode = registrationCode,
                        requestingUid = requestingUid,
                        isAdministrator = isAdministrator
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun update(id: Long, administratorUid: String, name: String): Result<Unit> {
        return try {
            val response = remoteInstitutionDataSource.update(
                id = id,
                request = UpdateInstitutionRequest(
                    administratorUid = administratorUid,
                    name = name
                )
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

    private fun getAllLocal(response: MutableList<InstitutionRoomEntity>): Result<MutableList<Institution>> {
        val institutions = mutableListOf<Institution>()
        for (institution in response) {
            institutions.add(
                convertToModel(institution)
            )
        }
        return Result.success(institutions)
    }

    private fun getAllRemote(response: Response<GetAllInstitutionsResponse>): Result<MutableList<Institution>> {
        return if (response.isSuccessful) {
            val institutions = mutableListOf<Institution>()
            response.body()?.let {
                for (institution in it.institutions) {
                    institutions.add(
                        convertToModel(institution)
                    )
                }
            }
            Result.success(institutions)
        } else {
            Result.failure(Exception(response.errorBody()?.string()))
        }
    }

    private fun getLocal(response: InstitutionRoomEntity?): Result<Institution?> {
        return response?.let {
            Result.success(
                convertToModel(it)
            )
        } ?: Result.failure(Exception("A instituição não foi encontrada."))
    }

    private fun getRemote(response: Response<InstitutionApiModel>): Result<Institution?> {
        return if (response.isSuccessful) {
            response.body()!!.let {
                Result.success(
                    convertToModel(it)
                )
            }
        } else {
            Result.failure(Exception(response.errorBody()?.string()))
        }
    }

}