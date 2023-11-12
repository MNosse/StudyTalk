package br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase

import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.repository.ParticipantRepository

class ChangeAnAnswerLikeStatusUseCase(
    private val repository: ParticipantRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(input: Input): Output {
        return Output(
            repository.changeAnAnswerLikeStatus(
                participantUid = input.participantUid,
                answerId = input.answerId
            )
        )
    }

    data class Input(
        val participantUid: String,
        val answerId: Long
    )

    data class Output(val result: Result<Unit>)
}