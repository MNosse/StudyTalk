package br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel

import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.model.Administrator
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.useCase.AdministratorUseCases
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.model.Participant
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.GetByUidUseCase
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.ParticipantUseCases
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking

class StudyTalkAdministratorHandler(
    private val administratorUseCases: AdministratorUseCases
) {
    private val _currentAdministrator: Administrator?
        get() {
            return runBlocking {
                FirebaseAuth.getInstance().uid?.let { uid ->
                    administratorUseCases.getByUidUseCase(
                        br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.useCase.GetByUidUseCase.Input(
                            uid = uid
                        )
                    ).result.getOrNull()
                }
            }
        }
    val currentAdministrator: Administrator? = _currentAdministrator
}