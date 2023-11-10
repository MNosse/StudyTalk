package br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.useCase

import br.com.udesc.eso.tcc.studytalk.core.domain.model.Subject
import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.repository.QuestionRepository

class UpdateUseCase(
    private val repository: QuestionRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(input: Input): Output {
        return Output(
            repository.update(
                id = input.id,
                title = input.title,
                description = input.description,
                subjects = input.subjects?.toMutableList(),
                participantUid = input.participantUid
            )
        )
    }

    data class Input(
        val id: Long,
        val title: String?,
        val description: String?,
        val subjects: List<Subject>?,
        val participantUid: String
    )

    data class Output(val result: Result<Unit>)
}