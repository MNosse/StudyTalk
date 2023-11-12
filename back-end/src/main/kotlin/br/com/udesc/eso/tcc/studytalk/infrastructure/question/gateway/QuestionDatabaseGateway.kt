package br.com.udesc.eso.tcc.studytalk.infrastructure.question.gateway

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.question.gateway.QuestionGateway
import br.com.udesc.eso.tcc.studytalk.entity.question.model.Question
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.question.gateway.converter.convert
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class QuestionDatabaseGateway(
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository
) : QuestionGateway {
    override fun delete(id: Long) {
        questionRepository.findById(id).getOrNull()?.let { question ->
            participantRepository.findAll().forEach {
                it.favoriteQuestions.remove(question)
                participantRepository.save(it)
            }
            questionRepository.delete(question)
        }
    }

    override fun getAllByInstitutionId(id: Long): MutableList<Question> {
        return questionRepository.findAllByInstitutionIdOrderById(id).map {
            convert(it)
        }.toMutableList()
    }

    override fun getById(id: Long): Question? {
        return questionRepository.findById(id).getOrNull()?.let {
            convert(it)
        }
    }

    override fun update(id: Long, title: String?, description: String?, subjects: MutableList<Subject>?) {
        questionRepository.findById(id).getOrNull()?.let { questionSchema ->
            if(title?.isNotBlank() == true) {
                questionSchema.title = title
            }
            if(description?.isNotBlank() == true) {
                questionSchema.description = description
            }
            subjects?.let { subjects ->
                (subjects.size > 0 && questionSchema.subjects != subjects).let {
                    questionSchema.subjects.clear()
                    subjects.forEach { questionSchema.subjects.add(it) }
                }
            }
            questionRepository.save(questionSchema)
        }
    }
}