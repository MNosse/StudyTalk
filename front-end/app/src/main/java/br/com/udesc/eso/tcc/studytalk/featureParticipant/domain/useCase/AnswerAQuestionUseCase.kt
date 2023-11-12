package br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase

import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.repository.ParticipantRepository

class AnswerAQuestionUseCase(
    private val repository: ParticipantRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(input: Input): Output {
        return Output(
            repository.answerAQuestion(
                participantUid = input.participantUid,
                questionId = input.questionId,
                description = input.description
            )
        )
    }

    data class Input(
        val participantUid: String,
        val questionId: Long,
        val description: String
    )

    data class Output(val result: Result<Unit>)
}