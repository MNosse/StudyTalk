package br.com.udesc.eso.tcc.studytalk.featureAnswer.data.dataSource

import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.model.AnswerApiModel
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.request.UpdateAnswerRequest
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.response.GetAllAnswersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteAnswerDataSource {

    @DELETE("answes/{id}/")
    suspend fun delete(
        @Path("id") id: Long,
        @Query("participantUid") participantUid: String
    ): Response<Unit>

    @GET("answers/question/{id}/")
    suspend fun getAllByQuestion(
        @Path("id") id: Long,
        @Query("participantUid") participantUid: String
    ): Response<GetAllAnswersResponse>

    @GET("answers/{id}/")
    suspend fun getById(
        @Path("id") id: Long,
        @Query("participantUid") participantUid: String
    ): Response<AnswerApiModel>

    @PUT("answers/{id}/")
    suspend fun update(
        @Path("id") id: Long,
        @Body request: UpdateAnswerRequest
    ): Response<Unit>

}

