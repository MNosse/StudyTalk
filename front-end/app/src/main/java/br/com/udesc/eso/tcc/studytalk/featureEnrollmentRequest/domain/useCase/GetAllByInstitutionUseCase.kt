package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.useCase

import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.model.EnrollmentRequest
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.repository.EnrollmentRequestRepository

class GetAllByInstitutionUseCase(
    private val repository: EnrollmentRequestRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(input: Input): Output {
        return Output(
            repository.getAllByInstitution(
                id = input.id,
                requestingParticipantUid = input.requestingParticipantUid
            )
        )
    }

    data class Input(
        val id: Long,
        val requestingParticipantUid: String
    )

    data class Output(val result: Result<MutableList<EnrollmentRequest>>)
}