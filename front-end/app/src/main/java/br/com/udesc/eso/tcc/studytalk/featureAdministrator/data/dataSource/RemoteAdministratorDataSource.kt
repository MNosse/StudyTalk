package br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.dataSource

import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.model.AdministratorApiModel
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.request.CreateAdministratorRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RemoteAdministratorDataSource {

    @POST("administrators/")
    suspend fun create(@Body request: CreateAdministratorRequest): Response<AdministratorApiModel>

    @GET("administrators/{uid}/")
    suspend fun getByUid(@Path("uid") uid: String): Response<AdministratorApiModel>

}