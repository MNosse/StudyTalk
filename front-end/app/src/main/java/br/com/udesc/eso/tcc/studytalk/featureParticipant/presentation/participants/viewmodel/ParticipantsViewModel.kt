package br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.participants.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkAdministratorHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkEvent
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkParticipantHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.GetAllByInstitutionUseCase
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.GetAllUseCase
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.ParticipantUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipantsViewModel @Inject constructor(
    private val participantUseCases: ParticipantUseCases,
    studyTalkAdministratorHandler: StudyTalkAdministratorHandler,
    studyTalkParticipantHandler: StudyTalkParticipantHandler
) : StudyTalkViewModel() {
    private val currentAdministratorUid: String?
    private val currentParticipantUid: String?

    private val _state = mutableStateOf(ParticipantsState())
    val state: State<ParticipantsState> = _state

    init {
        val currentAdministrator = studyTalkAdministratorHandler.currentAdministrator?.copy()
        currentAdministratorUid = currentAdministrator?.uid
        val currentParticipant = studyTalkParticipantHandler.currentParticipant?.copy()
        currentParticipantUid = currentParticipant?.uid
        var institutionId: Long? = null
        currentParticipant?.let { participant ->
            institutionId = participant.institution!!.id
        }
        viewModelScope.launch {
            getParticipants(institutionId)
        }
    }

    private suspend fun getParticipants(institutionId: Long?) {
        val result = if (institutionId != null) {
            participantUseCases.getAllByInstitutionUseCase(
                input = GetAllByInstitutionUseCase.Input(
                    requestingUid = currentParticipantUid!!,
                    isAdministrator = false,
                    institutionId = institutionId
                )
            ).result
        } else {
            participantUseCases.getAllUseCase(
                input = GetAllUseCase.Input(
                    administratorUid = currentAdministratorUid!!
                )
            ) .result
        }
        result.let {
            if (it.isSuccess) {
                it.getOrNull()!!.let { participants ->
                    _state.value = state.value.copy(
                        participants = participants.reversed()
                    )
                }
            } else {
                onEvent(StudyTalkEvent.EnteredExceptionMessage(it.exceptionOrNull()!!.message!!))
            }
        }
    }
}