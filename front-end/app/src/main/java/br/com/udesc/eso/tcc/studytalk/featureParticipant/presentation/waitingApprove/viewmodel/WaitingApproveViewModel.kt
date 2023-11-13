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
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaitingApproveViewModel @Inject constructor(
    private val participantUseCases: ParticipantUseCases,
    sharedPreferences: SharedPreferences,
    studyTalkParticipantHandler: StudyTalkParticipantHandler
) : StudyTalkViewModel() {
    private val currentParticipantUid: String

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        currentParticipantUid = studyTalkParticipantHandler.currentParticipant!!.uid
        viewModelScope.launch { checkApprovedState() }
    }

    fun onEvent(event: WaitingApproveEvent) {
        when (event) {
            is WaitingApproveEvent.Reload -> {
                viewModelScope.launch { checkApprovedState() }
            }

            is WaitingApproveEvent.SignOut -> {
                FirebaseAuth.getInstance().signOut()
                onEvent(StudyTalkEvent.EnteredRoute(BaseScreens.InitialActivity.route))
            }
        }
    }

    private suspend fun checkApprovedState() {
        _isLoading.value = true
        participantUseCases.getByUidUseCase(
            input = GetByUidUseCase.Input(
                participantToBeRetrievedUid = currentParticipantUid
            )
        ).result.let {
            if (it.isSuccess) {
                if (it.getOrNull()!!.institution != null) {
                    onEvent(StudyTalkEvent.EnteredRoute(BaseScreens.HomeScreen.route))
                }
            }
            _isLoading.value = false
        }
    }

}