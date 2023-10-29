package br.com.udesc.eso.tcc.studytalk.featureQuestion.data.dataSource

import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.model.QuestionApiModel
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.request.UpdateQuestionRequest
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.response.GetAllQuestionsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteQuestionDataSource {

    @DELETE("questions/{id}/")
    suspend fun delete(
        @Path("id") id: Long,
        @Query("participantUid") participantUid: String
    ): Response<Unit>

    @GET("questions/institution/{id}/")
    suspend fun getAllByInstitution(
        @Path("id") id: Long,
        @Query("participantUid") participantUid: String
    ): Response<GetAllQuestionsResponse>

    @GET("questions/{id}/")
    suspend fun getById(
        @Path("id") id: Long,
        @Query("participantUid") participantUid: String
    ): Response<QuestionApiModel>

    @PUT("questions/{id}/")
    suspend fun update(
        @Path("id") id: Long,
        @Body request: UpdateQuestionRequest
    ): Response<Unit>

}