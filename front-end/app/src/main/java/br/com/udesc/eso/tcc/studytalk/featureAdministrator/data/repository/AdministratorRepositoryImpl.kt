package br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.repository

import br.com.udesc.eso.tcc.studytalk.core.data.repository.BaseRepositoryImpl
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.converter.convertToModel
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.converter.convertToRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.dataSource.LocalAdministratorDataSource
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.dataSource.RemoteAdministratorDataSource
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.request.CreateAdministratorRequest
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.model.Administrator
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.repository.AdministratorRepository
import javax.inject.Inject


class AdministratorRepositoryImpl @Inject constructor(
    private val localAdministratorDataSource: LocalAdministratorDataSource,
    private val remoteAdministratorDataSource: RemoteAdministratorDataSource
) : AdministratorRepository, BaseRepositoryImpl() {

    override suspend fun create(uid: String): Result<Unit> {
        return try {
            val response =
                remoteAdministratorDataSource.create(request = CreateAdministratorRequest(uid = uid))
            if (response.isSuccessful) {
                response.body()?.let {
                    localAdministratorDataSource.create(
                        administratorRoomEntity = convertToRoomEntity(it)
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

    override suspend fun getAdministratorByUid(uid: String): Result<Administrator?> {
        return try {
            if (isOnline()) {
                val response = remoteAdministratorDataSource.getByUid(uid = uid)
                if (response.isSuccessful) {
                    response.body()!!.let {
                        Result.success(convertToModel(it))
                    }
                } else {
                    Result.failure(Exception(response.errorBody()?.string()))
                }
            } else {
                val response = localAdministratorDataSource.getByUid(uid = uid)
                response?.let {
                    Result.success(convertToModel(it))
                } ?: Result.failure(Exception("O administrador não foi encontrado."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}