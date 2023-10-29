package br.com.udesc.eso.tcc.studytalk.featureParticipant.data.repository

import br.com.udesc.eso.tcc.studytalk.core.domain.model.Privilege
import br.com.udesc.eso.tcc.studytalk.core.domain.model.Subject
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.model.Participant

interface ParticipantRepository {

    suspend fun answerAQuestion(
        participantUid: String,
        questionId: Long,
        description: String
    ): Result<Unit>

    suspend fun changeAnAnswerLikeStatus(
        participantUid: String,
        answerId: Long,
    ): Result<Unit>

    suspend fun changeAQuestionFavoriteStatus(
        participantUid: String,
        questionId: Long,
    ): Result<Unit>

    suspend fun create(
        registrationCode: String,
        uid: String,
        name: String
    ): Result<Unit>

    suspend fun delete(
        requestingParticipantUid: String,
        participantToBeDeletedUid: String
    ): Result<Unit>

    suspend fun doAQuestion(
        participantUid: String,
        title: String,
        description: String,
        subjects: MutableList<Subject>
    ): Result<Unit>

    suspend fun getAllByInstitution(
        requestingUid: String,
        isAdministrator: Boolean,
        institutionId: Long
    ): Result<MutableList<Participant>>

    suspend fun getAll(
        administratorUid: String
    ): Result<MutableList<Participant>>

    suspend fun getByUid(
        requestingUid: String,
        isAdministrator: Boolean,
        participantToBeRetrievedUid: String
    ): Result<Participant?>

    suspend fun updatePrivilege(
        requestingUid: String,
        isAdministrator: Boolean,
        participantToBeUpdatedUid: String,
        privilege: Privilege
    ): Result<Unit>

    suspend fun update(
        requestingParticipantUid: String,
        participantToBeUpdatedUid: String,
        name: String
    ): Result<Unit>

}