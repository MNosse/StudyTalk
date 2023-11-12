package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.useCase

import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.repository.EnrollmentRequestRepository

class ReproveUseCase(
    private val repository: EnrollmentRequestRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(input: Input): Output {
        return Output(
            repository.reprove(
                id = input.id,
                reproverUid = input.reproverUid
            )
        )
    }

    data class Input(
        val id: Long,
        val reproverUid: String
    )

    data class Output(val result: Result<Unit>)
}