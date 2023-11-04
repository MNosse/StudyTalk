package br.com.udesc.eso.tcc.studytalk.featureAnswer.data.repository

import br.com.udesc.eso.tcc.studytalk.core.data.repository.BaseRepositoryImpl
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.converter.convertToModel
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.dataSource.LocalAnswerDataSource
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.dataSource.RemoteAnswerDataSource
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.entity.AnswerRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.model.AnswerApiModel
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.request.UpdateAnswerRequest
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.response.GetAllAnswersResponse
import br.com.udesc.eso.tcc.studytalk.featureAnswer.domain.model.Answer
import br.com.udesc.eso.tcc.studytalk.featureAnswer.domain.repository.AnswerRepository
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.dataSource.LocalParticipantDataSource
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.dataSource.LocalQuestionDataSource
import retrofit2.Response
import javax.inject.Inject

class AnswerRepositoryImpl @Inject constructor(
    private val localAnswerDataSource: LocalAnswerDataSource,
    private val localParticipantDataSource: LocalParticipantDataSource,
    private val localQuestionDataSource: LocalQuestionDataSource,
    private val remoteAnswerDataSource: RemoteAnswerDataSource
) : AnswerRepository, BaseRepositoryImpl() {

    override suspend fun delete(id: Long, participantUid: String): Result<Unit> {
        return try {
            val response = remoteAnswerDataSource.delete(
                id = id,
                participantUid = participantUid
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

    override suspend fun getAllByQuestion(
        id: Long,
        participantUid: String
    ): Result<MutableList<Answer>> {
        return try {
            if (isOnline()) {
                getAllRemote(
                    remoteAnswerDataSource.getAllByQuestion(
                        id = id,
                        participantUid = participantUid
                    )
                )
            } else {
                getAllLocal(
                    localAnswerDataSource.getAllByQuestion(
                        id = id,
                        participantUid = participantUid
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getById(id: Long, participantUid: String): Result<Answer?> {
        return try {
            if (isOnline()) {
                getRemote(
                    remoteAnswerDataSource.getById(
                        id = id,
                        participantUid = participantUid
                    )
                )
            } else {
                getLocal(
                    localAnswerDataSource.getById(
                        id = id,
                        participantUid = participantUid
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun update(
        id: Long,
        description: String,
        participantUid: String
    ): Result<Unit> {
        return try {
            val response = remoteAnswerDataSource.update(
                id = id,
                request = UpdateAnswerRequest(
                    description = description,
                    participantUid = participantUid
                )
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

    private suspend fun getAllLocal(response: MutableList<AnswerRoomEntity>): Result<MutableList<Answer>> {
        val answers = mutableListOf<Answer>()
        for (answer in response) {
            val question = localQuestionDataSource.getByIdRelationship(answer.participantId)!!
            val participant = localParticipantDataSource.getByIdRelationship(answer.participantId)!!
            answers.add(
                convertToModel(
                    answer,
                    question,
                    participant
                )
            )
        }
        return Result.success(answers)
    }

    private fun getAllRemote(response: Response<GetAllAnswersResponse>): Result<MutableList<Answer>> {
        return if (response.isSuccessful) {
            val answers = mutableListOf<Answer>()
            response.body()?.let {
                for (answer in it.answers) {
                    answers.add(convertToModel(answer))
                }
            }
            Result.success(answers)
        } else {
            Result.failure(Exception("$response.errorBody()"))
        }
    }

    private suspend fun getLocal(response: AnswerRoomEntity?): Result<Answer?> {
        return response?.let {
            val question = localQuestionDataSource.getByIdRelationship(it.participantId)!!
            val participant = localParticipantDataSource.getByIdRelationship(it.participantId)!!
            Result.success(
                convertToModel(
                    it,
                    question,
                    participant
                )
            )
        } ?: Result.failure(Exception("A resposta não foi encontrada."))
    }

    private fun getRemote(response: Response<AnswerApiModel>): Result<Answer?> {
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