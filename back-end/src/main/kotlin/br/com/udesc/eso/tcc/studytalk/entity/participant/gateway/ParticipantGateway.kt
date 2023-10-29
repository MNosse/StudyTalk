package br.com.udesc.eso.tcc.studytalk.entity.participant.gateway

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer
import br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant
import br.com.udesc.eso.tcc.studytalk.entity.question.model.Question

interface ParticipantGateway {
    fun answerAQuestion(participantUid: String, questionId: Long, description: String): Answer?
    fun changeAnAnswerLikeStatus(participantUid: String, answerId: Long)
    fun changeAQuestionFavoriteStatus(participantUid: String, questionId: Long)
    fun create(registrationCode: String, uid: String, name: String): Participant?
    fun delete(uid: String)
    fun doAQuestion(participantUid: String, title: String, description: String, subjects: MutableList<Subject>): Question?
    fun getAll(): MutableList<Participant>
    fun getAllByInstitutionId(id: Long): MutableList<Participant>
    fun getByUid(uid: String): Participant?
    fun update(uid: String, name: String)
    fun updatePrivilege(uid: String, privilege: Privilege)
}