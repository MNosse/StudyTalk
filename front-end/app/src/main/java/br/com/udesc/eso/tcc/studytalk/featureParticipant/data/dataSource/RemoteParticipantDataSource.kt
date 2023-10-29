package br.com.udesc.eso.tcc.studytalk.featureParticipant.data.dataSource

import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.model.AnswerApiModel
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.model.ParticipantApiModel
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.request.AnswerAQuestionRequest
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.request.CreateParticipantRequest
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.request.DoAQuestionRequest
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.request.UpdateParticipantPrivilegeRequest
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.request.UpdateParticipantRequest
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.response.GetAllParticipantsResponse
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.model.QuestionApiModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteParticipantDataSource {

    @POST("participants/{participantUid}/questions/{questionId}/answers/")
    suspend fun answerAQuestion(
        @Path("participantUid") participantUid: String,
        @Path("questionId") questionId: Long,
        @Body request: AnswerAQuestionRequest
    ): Response<AnswerApiModel>

    @PUT("participants/{participantUid}/answers/{answerId}/change-like-status/")
    suspend fun changeAnAnswerLikeStatus(
        @Path("participantUid") participantUid: String,
        @Path("answerId") answerId: Long
    ): Response<Unit>

    @PUT("participants/{participantUid}/questions/{questionId}/change-favorite-status/")
    suspend fun changeAQuestionFavoriteStatus(
        @Path("participantUid") participantUid: String,
        @Path("questionId") questionId: Long
    ): Response<Unit>

    @POST("participants/")
    suspend fun create(@Body request: CreateParticipantRequest): Response<ParticipantApiModel>

    @DELETE("participants/{participantToBeDeletedUid}/")
    suspend fun delete(
        @Path("participantToBeDeletedUid") participantToBeDeletedUid: String,
        @Query("requestingParticipantUid") requestingParticipantUid: String
    ): Response<Unit>

    @POST("participants/{participantUid}/questions/")
    suspend fun doAQuestion(
        @Path("participantUid") participantUid: String,
        @Body request: DoAQuestionRequest
    ): Response<QuestionApiModel>

    @GET("participants/")
    suspend fun getAll(@Query("administratorUid") administratorUid: String): Response<GetAllParticipantsResponse>

    @GET("participants/institution/{institutionId}/")
    suspend fun getAllByInstitution(
        @Path("institutionId") institutionId: Long,
        @Query("requestingUid") requestingUid: String,
        @Query("isAdministrator") isAdministrator: Boolean
    ): Response<GetAllParticipantsResponse>

    @GET("participants/{participantToBeRetrievedUid}/")
    suspend fun getByUid(
        @Path("participantToBeRetrievedUid") participantToBeRetrievedUid: String,
        @Query("requestingUid") requestingUid: String,
        @Query("isAdministrator") isAdministrator: Boolean
    ): Response<ParticipantApiModel>

    @PUT("participants/{participantToBeUpdatedUid}/")
    suspend fun update(
        @Path("participantToBeUpdatedUid") participantToBeUpdatedUid: String,
        @Body request: UpdateParticipantRequest
    ): Response<Unit>

    @PUT("participants/{participantToBeUpdatedUid}/privilege/")
    suspend fun updatePrivilege(
        @Path("participantToBeUpdatedUid") participantToBeUpdatedUid: String,
        @Body request: UpdateParticipantPrivilegeRequest
    ): Response<Unit>

}


