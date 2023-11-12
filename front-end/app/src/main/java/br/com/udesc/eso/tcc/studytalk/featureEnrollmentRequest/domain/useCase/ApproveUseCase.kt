package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.useCase

import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.repository.EnrollmentRequestRepository

class ApproveUseCase(
    private val repository: EnrollmentRequestRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(input: Input): Output {
        return Output(
            repository.approve(
                id = input.id,
                approverUid = input.approverUid
            )
        )
    }

    data class Input(
        val id: Long,
        val approverUid: String
    )

    data class Output(val result: Result<Unit>)
}