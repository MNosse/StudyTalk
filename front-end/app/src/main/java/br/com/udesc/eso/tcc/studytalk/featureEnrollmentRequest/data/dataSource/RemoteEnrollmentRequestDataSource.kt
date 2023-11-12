package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.dataSource

import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.model.EnrollmentRequestApiModel
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.response.GetAllEnrollmentRequestiesResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteEnrollmentRequestDataSource {

    @DELETE("enrollment-requesties/approve/{id}/")
    suspend fun approve(
        @Path("id") id: Long,
        @Query("approverUid") approverUid: String
    ): Response<Unit>

    @GET("enrollment-requesties/institution/{id}/")
    suspend fun getAllByInstitution(
        @Path("id") id: Long,
        @Query("requestingParticipantUid") requestingParticipantUid: String
    ): Response<GetAllEnrollmentRequestiesResponse>

    @GET("enrollment-requesties/{id}/")
    suspend fun getById(
        @Path("id") id: Long,
        @Query("requestingParticipantUid") requestingParticipantUid: String
    ): Response<EnrollmentRequestApiModel>

    @GET("enrollment-requesties/participant/{id}/")
    suspend fun getByParticipant(
        @Path("id") id: Long,
        @Query("requestingParticipantUid") requestingParticipantUid: String
    ): Response<EnrollmentRequestApiModel>

    @DELETE("enrollment-requesties/reprove/{id}/")
    suspend fun reprove(
        @Path("id") id: Long,
        @Query("reproverUid") reproverUid: String
    ): Response<Unit>

}

