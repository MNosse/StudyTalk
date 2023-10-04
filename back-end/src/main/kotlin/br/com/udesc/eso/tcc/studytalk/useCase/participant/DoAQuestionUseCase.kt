package br.com.udesc.eso.tcc.studytalk.useCase.participant

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import org.springframework.stereotype.Service

@Service
class DoAQuestionUseCase(private val participantGateway: ParticipantGateway) {
    @Throws(ParticipantNotFoundException::class)
    fun execute(input: Input) {
        participantGateway.getByUid(input.participantUid)?.let {
            participantGateway.doAQuestion(
                participantUid = input.participantUid,
                title = input.title,
                description = input.description,
                subjects = input.subjects
            )
        } ?: throw ParticipantNotFoundException()
    }

    data class Input(
        val participantUid: String,
        val title: String,
        val description: String,
        val subjects: MutableList<Subject>
    )
}