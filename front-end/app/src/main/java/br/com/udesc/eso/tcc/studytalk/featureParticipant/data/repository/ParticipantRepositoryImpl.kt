package br.com.udesc.eso.tcc.studytalk.featureParticipant.data.repository

import br.com.udesc.eso.tcc.studytalk.core.data.repository.BaseRepositoryImpl
import br.com.udesc.eso.tcc.studytalk.core.domain.model.Privilege
import br.com.udesc.eso.tcc.studytalk.core.domain.model.Subject
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.dataSource.LocalAnswerDataSource
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.dataSource.LocalInstitutionDataSource
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.converter.convertToModel
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.converter.convertToRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.dataSource.LocalParticipantDataSource
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.dataSource.RemoteParticipantDataSource
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.entity.ParticipantRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.model.ParticipantApiModel
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.request.AnswerAQuestionRequest
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.request.CreateParticipantRequest
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.request.DoAQuestionRequest
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.request.UpdateParticipantPrivilegeRequest
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.request.UpdateParticipantRequest
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.response.GetAllParticipantsResponse
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.model.Participant
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.repository.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.dataSource.LocalQuestionDataSource
import retrofit2.Response
import javax.inject.Inject

class ParticipantRepositoryImpl @Inject constructor(
    private val localAnswerDataSource: LocalAnswerDataSource,
    private val localInstitutionDataSource: LocalInstitutionDataSource,
    private val localParticipantDataSource: LocalParticipantDataSource,
    private val localQuestionDataSource: LocalQuestionDataSource,
    private val remoteParticipantDataSource: RemoteParticipantDataSource
) : ParticipantRepository, BaseRepositoryImpl() {

    override suspend fun answerAQuestion(
        participantUid: String,
        questionId: Long,
        description: String
    ): Result<Unit> {
        return try {
            val response = remoteParticipantDataSource.answerAQuestion(
                participantUid = participantUid,
                questionId = questionId,
                request = AnswerAQuestionRequest(
                    description = description
                )
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    localAnswerDataSource.create(
                        answerRoomEntity = br.com.udesc.eso.tcc.studytalk.featureAnswer.data.converter.convertToRoomEntity(
                            answer = it
                        )
                    )
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("$response.errorBody()"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun changeAnAnswerLikeStatus(
        participantUid: String,
        answerId: Long
    ): Result<Unit> {
        return try {
            val response = remoteParticipantDataSource.changeAnAnswerLikeStatus(
                participantUid = participantUid,
                answerId = answerId
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

    override suspend fun changeAQuestionFavoriteStatus(
        participantUid: String,
        questionId: Long
    ): Result<Unit> {
        return try {
            val response = remoteParticipantDataSource.changeAQuestionFavoriteStatus(
                participantUid = participantUid,
                questionId = questionId
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

    override suspend fun create(registrationCode: String, uid: String, name: String): Result<Unit> {
        return try {
            val response = remoteParticipantDataSource.create(
                request = CreateParticipantRequest(
                    registrationCode = registrationCode,
                    uid = uid,
                    name = name
                )
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    localParticipantDataSource.create(participantRoomEntity = convertToRoomEntity(it))
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("$response.errorBody()"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun delete(
        requestingParticipantUid: String,
        participantToBeDeletedUid: String
    ): Result<Unit> {
        return try {
            val response = remoteParticipantDataSource.delete(
                participantToBeDeletedUid = participantToBeDeletedUid,
                requestingParticipantUid = requestingParticipantUid
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

    override suspend fun doAQuestion(
        participantUid: String,
        title: String,
        description: String,
        subjects: MutableList<Subject>
    ): Result<Unit> {
        return try {
            val response = remoteParticipantDataSource.doAQuestion(
                participantUid = participantUid,
                request = DoAQuestionRequest(
                    title = title,
                    description = description,
                    subjects = subjects
                )
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    localQuestionDataSource.create(
                        questionRoomEntity = br.com.udesc.eso.tcc.studytalk.featureQuestion.data.converter.convertToRoomEntity(
                            question = it
                        )
                    )
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("$response.errorBody()"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllByInstitution(
        requestingUid: String,
        isAdministrator: Boolean,
        institutionId: Long
    ): Result<MutableList<Participant>> {
        return try {
            if (isOnline()) {
                getAllRemote(
                    remoteParticipantDataSource.getAllByInstitution(
                        institutionId = institutionId,
                        requestingUid = requestingUid,
                        isAdministrator = isAdministrator
                    )
                )

            } else {
                getAllLocal(
                    localParticipantDataSource.getAllByInstitution(
                        institutionId = institutionId,
                        requestingUid = requestingUid,
                        isAdministrator = isAdministrator
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAll(administratorUid: String): Result<MutableList<Participant>> {
        return try {
            if (isOnline()) {
                getAllRemote(remoteParticipantDataSource.getAll(administratorUid = administratorUid))
            } else {
                getAllLocal(localParticipantDataSource.getAll(administratorUid = administratorUid))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getByUid(
        requestingUid: String,
        isAdministrator: Boolean,
        participantToBeRetrievedUid: String
    ): Result<Participant?> {
        return try {
            if (isOnline()) {
                getRemote(
                    remoteParticipantDataSource.getByUid(
                        participantToBeRetrievedUid = participantToBeRetrievedUid,
                        requestingUid = requestingUid,
                        isAdministrator = isAdministrator
                    )
                )
            } else {
                getLocal(
                    localParticipantDataSource.getByUid(
                        participantToBeRetrievedUid = participantToBeRetrievedUid,
                        requestingUid = requestingUid,
                        isAdministrator = isAdministrator
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePrivilege(
        requestingUid: String,
        isAdministrator: Boolean,
        participantToBeUpdatedUid: String,
        privilege: Privilege
    ): Result<Unit> {
        return try {
            val response = remoteParticipantDataSource.updatePrivilege(
                participantToBeUpdatedUid = participantToBeUpdatedUid,
                request = UpdateParticipantPrivilegeRequest(
                    requestingUid = requestingUid,
                    isAdministrator = isAdministrator,
                    privilege = privilege
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

    override suspend fun update(
        requestingParticipantUid: String,
        participantToBeUpdatedUid: String,
        name: String
    ): Result<Unit> {
        return try {
            val response = remoteParticipantDataSource.update(
                participantToBeUpdatedUid = participantToBeUpdatedUid,
                request = UpdateParticipantRequest(
                    requestingParticipantUid = requestingParticipantUid,
                    name = name
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

    private suspend fun getAllLocal(response: MutableList<ParticipantRoomEntity>): Result<MutableList<Participant>> {
        val participants = mutableListOf<Participant>()
        for (participant in response) {
            val institution = participant.institutionId?.let {
                localInstitutionDataSource.getByIdRelationship(it)
            }
            val favoriteQuestions = participant.favoriteQuestions.map {
                localQuestionDataSource.getByIdRelationship(it)!!
            }.toMutableList()
            val likedAnswers = participant.likedAnswers.map {
                localAnswerDataSource.getByIdRelationship(it)!!
            }.toMutableList()
            participants.add(
                convertToModel(participant, institution, favoriteQuestions, likedAnswers)
            )
        }
        return Result.success(participants)
    }

    private fun getAllRemote(response: Response<GetAllParticipantsResponse>): Result<MutableList<Participant>> {
        return if (response.isSuccessful) {
            val participants = mutableListOf<Participant>()
            response.body()?.let {
                for (participant in it.participants) {
                    participants.add(
                        convertToModel(participant)
                    )
                }
            }
            Result.success(participants)
        } else {
            Result.failure(Exception("$response.errorBody()"))
        }
    }

    private suspend fun getLocal(response: ParticipantRoomEntity?): Result<Participant?> {
        return response?.let {
            val institution = it.institutionId?.let {
                localInstitutionDataSource.getByIdRelationship(it)
            }
            val favoriteQuestions = it.favoriteQuestions.map {
                localQuestionDataSource.getByIdRelationship(it)!!
            }.toMutableList()
            val likedAnswers = it.likedAnswers.map {
                localAnswerDataSource.getByIdRelationship(it)!!
            }.toMutableList()
            Result.success(
                convertToModel(it, institution, favoriteQuestions, likedAnswers)
            )
        } ?: Result.failure(Exception("o participante não foi encontrado."))
    }

    private fun getRemote(response: Response<ParticipantApiModel>): Result<Participant?> {
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