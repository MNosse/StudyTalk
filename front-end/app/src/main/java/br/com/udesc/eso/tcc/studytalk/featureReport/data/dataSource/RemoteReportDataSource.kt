package br.com.udesc.eso.tcc.studytalk.featureReport.data.dataSource

import br.com.udesc.eso.tcc.studytalk.featureReport.data.model.ReportApiModel
import br.com.udesc.eso.tcc.studytalk.featureReport.data.request.CreateReportRequest
import br.com.udesc.eso.tcc.studytalk.featureReport.data.response.GetAllReportsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteReportDataSource {

    @PUT("reports/approve/{id}/")
    suspend fun approve(
        @Path("id") id: Long,
        @Query("approverUid") approverUid: String
    ): Response<Unit>

    @POST("reports/")
    suspend fun create(
        @Body request: CreateReportRequest
    ): Response<ReportApiModel>

    @GET("reports/institution/{id}/")
    suspend fun getAllByInstitution(
        @Path("id") id: Long,
        @Query("requestingParticipantUid") requestingParticipantUid: String
    ): Response<GetAllReportsResponse>

    @GET("reports/{id}/")
    suspend fun getById(
        @Path("id") id: Long,
        @Query("requestingParticipantUid") requestingParticipantUid: String
    ): Response<ReportApiModel>

    @PUT("reports/reprove/{id}/")
    suspend fun reprove(
        @Path("id") id: Long,
        @Query("reproverUid") reproverUid: String
    ): Response<Unit>

}