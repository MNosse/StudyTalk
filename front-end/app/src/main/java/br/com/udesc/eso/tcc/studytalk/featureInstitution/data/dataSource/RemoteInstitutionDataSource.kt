package br.com.udesc.eso.tcc.studytalk.featureInstitution.data.dataSource

import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.model.InstitutionApiModel
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.request.CreateInstitutionRequest
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.request.UpdateInstitutionRequest
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.response.GetAllInstitutionsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteInstitutionDataSource {

    @POST("institutions/")
    suspend fun create(@Body request: CreateInstitutionRequest): Response<InstitutionApiModel>

    @DELETE("institutions/{id}/")
    suspend fun delete(
        @Path("id") id: Long,
        @Query("administratorUid") administratorUid: String
    ): Response<Unit>

    @GET("institutions/")
    suspend fun getAll(@Query("administratorUid") administratorUid: String): Response<GetAllInstitutionsResponse>

    @GET("institutions/{id}/")
    suspend fun getById(
        @Path("id") id: Long,
        @Query("requestingUid") requestingUid: String,
        @Query("isAdministrator") isAdministrator: Boolean
    ): Response<InstitutionApiModel>

    @GET("institutions/registration_code/{registrationCode}/")
    suspend fun getByRegistrationCode(
        @Path("registrationCode") registrationCode: String,
        @Query("requestingUid") requestingUid: String,
        @Query("isAdministrator") isAdministrator: Boolean
    ): Response<InstitutionApiModel>

    @PUT("institutions/{id}/")
    suspend fun update(
        @Path("id") id: Long,
        @Body request: UpdateInstitutionRequest
    ): Response<Unit>

}

