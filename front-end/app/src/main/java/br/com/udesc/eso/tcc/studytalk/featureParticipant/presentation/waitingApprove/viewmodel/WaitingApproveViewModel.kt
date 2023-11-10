package br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.waitingApprove.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.base.BaseScreens
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkAdministratorHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkEvent
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkParticipantHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.GetByUidUseCase
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.ParticipantUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaitingApproveViewModel @Inject constructor(
    private val participantUseCases: ParticipantUseCases,
    sharedPreferences: SharedPreferences,
    studyTalkAdministratorHandler: StudyTalkAdministratorHandler,
    studyTalkParticipantHandler: StudyTalkParticipantHandler
) : StudyTalkViewModel(studyTalkAdministratorHandler, studyTalkParticipantHandler) {

    private val _currentUid = mutableStateOf("")
    val currentUid: State<String> = _currentUid

    init {
        _currentUid.value = sharedPreferences.getString("current_uid", "")!!
        viewModelScope.launch { checkApprovedState() }
    }

    private suspend fun checkApprovedState() {
        participantUseCases.getByUidUseCase(
            input = GetByUidUseCase.Input(
                participantToBeRetrievedUid = currentUid.value
            )
        ).result.let {
            if (it.isSuccess) {
                if (it.getOrNull()!!.institution != null) {
                    onEvent(StudyTalkEvent.EnteredRoute(BaseScreens.HomeScreen.route))
                }
            }
        }
    }

}