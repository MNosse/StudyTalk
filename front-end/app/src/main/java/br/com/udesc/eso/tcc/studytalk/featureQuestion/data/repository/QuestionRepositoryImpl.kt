package br.com.udesc.eso.tcc.studytalk.featureQuestion.data.repository

import br.com.udesc.eso.tcc.studytalk.core.data.repository.BaseRepositoryImpl
import br.com.udesc.eso.tcc.studytalk.core.domain.model.Subject
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.dataSource.LocalInstitutionDataSource
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.dataSource.LocalParticipantDataSource
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.converter.convertToModel
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.dataSource.LocalQuestionDataSource
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.dataSource.RemoteQuestionDataSource
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.entity.QuestionRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.model.QuestionApiModel
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.request.UpdateQuestionRequest
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.response.GetAllQuestionsResponse
import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.model.Question
import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.repository.QuestionRepository
import retrofit2.Response
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val localInstitutionDataSource: LocalInstitutionDataSource,
    private val localParticipantDataSource: LocalParticipantDataSource,
    private val localQuestionDataSource: LocalQuestionDataSource,
    private val remoteQuestionDataSource: RemoteQuestionDataSource
) : QuestionRepository, BaseRepositoryImpl() {

    override suspend fun delete(id: Long, participantUid: String): Result<Unit> {
        return try {
            val response = remoteQuestionDataSource.delete(
                id = id,
                participantUid = participantUid
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

    override suspend fun getAllByInstitution(
        id: Long,
        participantUid: String
    ): Result<MutableList<Question>> {
        return try {
            if (isOnline()) {
                getAllRemote(
                    remoteQuestionDataSource.getAllByInstitution(
                        id = id,
                        participantUid = participantUid
                    )
                )
            } else {
                getAllLocal(
                    localQuestionDataSource.getAllByInstitution(
                        id = id,
                        participantUid = participantUid
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getById(id: Long, participantUid: String): Result<Question?> {
        return try {
            if (isOnline()) {
                getRemote(
                    remoteQuestionDataSource.getById(
                        id = id,
                        participantUid = participantUid
                    )
                )
            } else {
                getLocal(
                    localQuestionDataSource.getById(
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
        title: String?,
        description: String?,
        subjects: MutableList<Subject>?,
        participantUid: String
    ): Result<Unit> {
        return try {
            val response = remoteQuestionDataSource.update(
                id = id,
                request = UpdateQuestionRequest(
                    title = title,
                    description = description,
                    subjects = subjects,
                    participantUid = participantUid
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

    private suspend fun getAllLocal(response: MutableList<QuestionRoomEntity>): Result<MutableList<Question>> {
        val questions = mutableListOf<Question>()
        for (question in response) {
            val participant =
                localParticipantDataSource.getByIdRelationship(question.participantId)!!
            val institution =
                localInstitutionDataSource.getByIdRelationship(question.institutionId)!!
            questions.add(
                convertToModel(
                    question,
                    participant,
                    institution
                )
            )
        }
        return Result.success(questions)
    }

    private fun getAllRemote(response: Response<GetAllQuestionsResponse>): Result<MutableList<Question>> {
        return if (response.isSuccessful) {
            val questions = mutableListOf<Question>()
            response.body()?.let {
                for (question in it.questions) {
                    questions.add(convertToModel(question))
                }
            }
            Result.success(questions)
        } else {
            Result.failure(Exception(response.errorBody()?.string()))
        }
    }

    private suspend fun getLocal(response: QuestionRoomEntity?): Result<Question?> {
        return response?.let {
            val participant = localParticipantDataSource.getByIdRelationship(it.participantId)!!
            val institution = localInstitutionDataSource.getByIdRelationship(it.institutionId)!!
            Result.success(
                convertToModel(
                    it,
                    participant,
                    institution
                )
            )
        } ?: Result.failure(Exception("A questão não foi encontrada."))
    }

    private fun getRemote(response: Response<QuestionApiModel>): Result<Question?> {
        return if (response.isSuccessful) {
            response.body()!!.let {
                Result.success(convertToModel(it))
            }
        } else {
            Result.failure(Exception(response.errorBody()?.string()))
        }
    }

}