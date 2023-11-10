package br.com.udesc.eso.tcc.studytalk.infrastructure.participant.gateway

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant
import br.com.udesc.eso.tcc.studytalk.entity.question.model.Question
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.answer.AnswerRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.enrollmentRequest.EnrollmentRequestRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer.AnswerSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.enrollmentRequest.EnrollmentRequestSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.gateway.converter.convert
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class ParticipantDatabaseGateway(
    private val answerRepository: AnswerRepository,
    private val enrollmentRequestRepository: EnrollmentRequestRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository
) : ParticipantGateway {
    override fun answerAQuestion(participantUid: String, questionId: Long, description: String): Answer? {
        return participantRepository.findByUid(participantUid)?.let { participant ->
            questionRepository.findById(questionId).getOrNull()?.let {
                convert(
                    answerRepository.save(
                        AnswerSchema(
                            description = description,
                            question = it,
                            participant = participant
                        )
                    )
                )
            }
        }
    }

    override fun changeAnAnswerLikeStatus(participantUid: String, answerId: Long) {
        participantRepository.findByUid(participantUid)?.let { participant ->
            answerRepository.findById(answerId).getOrNull()?.let {
                when (containsAnswer(participant, it)) {
                    true -> {
                        it.likeCount--
                        answerRepository.save(it)
                        participant.likedAnswers.removeIf { answer -> answer.id == it.id }
                    }

                    false -> {
                        it.likeCount++
                        answerRepository.save(it)
                        participant.likedAnswers.add(it)
                    }
                }
                participantRepository.save(participant)
            }
        }
    }

    override fun changeAQuestionFavoriteStatus(participantUid: String, questionId: Long) {
        participantRepository.findByUid(participantUid)?.let { participant ->
            questionRepository.findById(questionId).getOrNull()?.let {
                when (containsQuestion(participant, it)) {
                    true -> {
                        participant.favoriteQuestions.removeIf { question -> question.id == it.id }
                    }

                    false -> {
                        participant.favoriteQuestions.add(it)
                    }
                }
                participantRepository.save(participant)
            }
        }
    }

    override fun create(registrationCode: String, uid: String, name: String): Participant? {
        var participant: ParticipantSchema? = null
        institutionRepository.findByRegistrationCode(registrationCode)?.let {
            if (participantRepository.findAllByInstitutionId(it.id).size > 0) {
                participant = participantRepository.save(ParticipantSchema(uid = uid, name = name))
                enrollmentRequestRepository.save(
                    EnrollmentRequestSchema(
                        institution = it,
                        participant = participant!!
                    )
                )
            } else {
                participant = participantRepository.save(
                    ParticipantSchema(
                        uid = uid,
                        name = name,
                        privilege = Privilege.PRINCIPAL,
                        institution = it
                    )
                )
            }
        }
        return participant?.let {
            convert(participant!!)
        }
    }

    override fun delete(uid: String) {
        participantRepository.findByUid(uid)?.let {
            participantRepository.delete(it)
        }
    }

    override fun doAQuestion(
        participantUid: String,
        title: String,
        description: String,
        subjects: MutableList<Subject>
    ): Question? {
        return participantRepository.findByUid(participantUid)?.let {
            br.com.udesc.eso.tcc.studytalk.infrastructure.question.gateway.converter.convert(
                questionRepository.save(
                    QuestionSchema(
                        title = title,
                        description = description,
                        subjects = subjects,
                        participant = it,
                        institution = it.institution!!
                    )
                )
            )
        }
    }

    override fun getAll(): MutableList<Participant> {
        return participantRepository.findAll().map {
            convert(it)
        }.toMutableList()
    }

    override fun getAllByInstitutionId(id: Long): MutableList<Participant> {
        return participantRepository.findAllByInstitutionId(id).map {
            convert(it)
        }.toMutableList()
    }

    override fun getByUid(uid: String): Participant? {
        return participantRepository.findByUid(uid)?.let {
            convert(it)
        }
    }

    override fun update(uid: String, name: String) {
        participantRepository.findByUid(uid)?.let {
            it.name = name
            participantRepository.save(it)
        }
    }

    override fun updatePrivilege(uid: String, privilege: Privilege) {
        participantRepository.findByUid(uid)?.let {
            it.privilege = privilege
            participantRepository.save(it)
        }
    }

    private fun containsAnswer(participantSchema: ParticipantSchema, answerSchema: AnswerSchema): Boolean {
        for (answer in participantSchema.likedAnswers) {
            if (answer.id == answerSchema.id) {
                return true
            }
        }
        return false
    }

    private fun containsQuestion(participantSchema: ParticipantSchema, questionSchema: QuestionSchema): Boolean {
        for (question in participantSchema.favoriteQuestions) {
            if (question.id == questionSchema.id) {
                return true
            }
        }
        return false
    }
}