package br.com.udesc.eso.tcc.studytalk.useCase.question

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionIsNotFromParticipantException
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.question.gateway.QuestionGateway
import org.springframework.stereotype.Service

@Service
class UpdateQuestionUseCase(private val questionGateway: QuestionGateway) {
    @Throws(
        QuestionIsNotFromParticipantException::class,
        QuestionNotFoundException::class
    )
    fun execute(input: Input) {
        questionGateway.getById(input.id)?.let {
            if (it.participant!!.uid == input.participantUid) {
                questionGateway.update(
                    id = input.id,
                    title = input.title,
                    description = input.description,
                    subjects = input.subjects,
                )
            } else throw QuestionIsNotFromParticipantException()
        } ?: throw QuestionNotFoundException()
    }

    data class Input(
        val id: Long,
        val title: String?,
        val description: String?,
        val subjects: MutableList<Subject>?,
        val participantUid: String
    )
}