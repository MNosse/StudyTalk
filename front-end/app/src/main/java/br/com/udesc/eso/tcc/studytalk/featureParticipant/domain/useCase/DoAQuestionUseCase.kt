package br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase

import br.com.udesc.eso.tcc.studytalk.core.domain.model.Subject
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.repository.ParticipantRepository

class DoAQuestionUseCase(
    private val repository: ParticipantRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(input: Input): Output {
        return Output(
            repository.doAQuestion(
                participantUid = input.participantUid,
                title = input.title,
                description = input.description,
                subjects = input.subjects.toMutableList()
            )
        )
    }

    data class Input(
        val participantUid: String,
        val title: String,
        val description: String,
        val subjects: List<Subject>
    )

    data class Output(val result: Result<Unit>)
}