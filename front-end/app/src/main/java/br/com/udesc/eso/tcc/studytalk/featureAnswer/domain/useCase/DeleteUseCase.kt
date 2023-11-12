package br.com.udesc.eso.tcc.studytalk.featureAnswer.domain.useCase

import br.com.udesc.eso.tcc.studytalk.featureAnswer.domain.repository.AnswerRepository

class DeleteUseCase(
    private val repository: AnswerRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(input: Input): Output {
        return Output(
            repository.delete(
                id = input.id,
                participantUid = input.participantUid
            )
        )
    }

    data class Input(
        val id: Long,
        val participantUid: String
    )

    data class Output(val result: Result<Unit>)
}