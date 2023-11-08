package br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase

import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.model.Participant
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.repository.ParticipantRepository

class GetByUidUseCase(
    private val repository: ParticipantRepository
) {

    @Throws(Exception::class)
    suspend operator fun invoke(input: Input): Output {
        return Output(
            repository.getByUid(
                requestingUid = input.requestingUid,
                isAdministrator = input.isAdministrator,
                participantToBeRetrievedUid = input.participantToBeRetrievedUid
            )
        )

    }

    data class Input(
        val requestingUid: String,
        val isAdministrator: Boolean,
        val participantToBeRetrievedUid: String
    ) {
        constructor(
            participantToBeRetrievedUid: String
        ) : this(participantToBeRetrievedUid, false, participantToBeRetrievedUid)
    }

    data class Output(val result: Result<Participant?>)

}