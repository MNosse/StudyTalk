package br.com.udesc.eso.tcc.studytalk.infrastructure.answer.gateway

import br.com.udesc.eso.tcc.studytalk.entity.answer.gateway.AnswerGateway
import br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer
import br.com.udesc.eso.tcc.studytalk.infrastructure.answer.gateway.converter.convert
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.answer.AnswerRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class AnswerDatabaseGateway(
    private val answerRepository: AnswerRepository,
    private val participantRepository: ParticipantRepository
) : AnswerGateway {
    override fun delete(id: Long) {
        answerRepository.findById(id).getOrNull()?.let { answer ->
            participantRepository.findAll().forEach {
                it.likedAnswers.remove(answer)
                participantRepository.save(it)
            }
            answerRepository.delete(answer)
        }
    }

    override fun getAllByQuestionId(id: Long): MutableList<Answer> {
        return answerRepository.findAllByQuestionIdOrderById(id).map {
            convert(it)
        }.toMutableList()
    }

    override fun getById(id: Long): Answer? {
        return answerRepository.findById(id).getOrNull()?.let {
            convert(it)
        }
    }

    override fun update(id: Long, description: String) {
        answerRepository.findById(id).getOrNull()?.let {
            it.description = description
            answerRepository.save(it)
        }
    }

}