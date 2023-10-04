package br.com.udesc.eso.tcc.studytalk.infrastructure.answer.gateway

import br.com.udesc.eso.tcc.studytalk.entity.answer.gateway.AnswerGateway
import br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer
import br.com.udesc.eso.tcc.studytalk.infrastructure.answer.gateway.converter.convert
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.answer.AnswerRepository
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class AnswerDatabaseGateway(
    private val answerRepository: AnswerRepository
) : AnswerGateway {
    override fun delete(id: Long) {
        answerRepository.deleteById(id)
    }

    override fun getAllByQuestionId(id: Long): MutableList<Answer> {
        return answerRepository.findAllByQuestionId(id).map {
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