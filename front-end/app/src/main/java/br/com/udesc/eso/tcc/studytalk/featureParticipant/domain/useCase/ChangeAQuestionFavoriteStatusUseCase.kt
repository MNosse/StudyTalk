package br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase

import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.repository.ParticipantRepository

class ChangeAQuestionFavoriteStatusUseCase(
    private val repository: ParticipantRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(input: Input): Output {
        return Output(
            repository.changeAQuestionFavoriteStatus(
                participantUid = input.participantUid,
                questionId = input.questionId
            )
        )
    }

    data class Input(
        val participantUid: String,
        val questionId: Long
    )

    data class Output(val result: Result<Unit>)
}