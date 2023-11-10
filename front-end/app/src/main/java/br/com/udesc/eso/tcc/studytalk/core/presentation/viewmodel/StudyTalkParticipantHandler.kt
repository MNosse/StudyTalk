package br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel

import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.model.Participant
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.GetByUidUseCase
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.ParticipantUseCases
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking

class StudyTalkParticipantHandler(
    private val participantUseCases: ParticipantUseCases
) {
    private val _currentParticipant: Participant?
        get() {
            return runBlocking {
                participantUseCases.getByUidUseCase(
                    GetByUidUseCase.Input(
                        participantToBeRetrievedUid = FirebaseAuth.getInstance().uid!!
                    )
                ).result.getOrNull()
            }
        }
    val currentParticipant: Participant? = _currentParticipant
}