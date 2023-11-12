package br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.profile.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.base.BaseScreens
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.initial.InitialScreens
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkAdministratorHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkEvent
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkParticipantHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.core.utils.UiText
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.CreateUseCase
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.ParticipantUseCases
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val participantUseCases: ParticipantUseCases,
    studyTalkAdministratorHandler: StudyTalkAdministratorHandler,
    studyTalkParticipantHandler: StudyTalkParticipantHandler
) : StudyTalkViewModel(studyTalkAdministratorHandler, studyTalkParticipantHandler) {

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _currentParticipantId = mutableLongStateOf(-1L)
    val currentParticipantId: State<Long> = _currentParticipantId

    private val _currentUid = mutableStateOf("")
    val currentUid: State<String> = _currentUid

    private val _isEditMode = mutableStateOf(false)
    val isEditMode: State<Boolean> = _isEditMode

    private val _registrationCode = mutableStateOf("")
    val registrationCode: State<String> = _registrationCode

    init {
        if (currentParticipant != null) {
            val currentParticipant = currentParticipant.copy()
            _currentParticipantId.longValue = currentParticipant.id
            _currentUid.value = currentParticipant.uid
            _name.value = currentParticipant.name
        } else {
            _currentUid.value = FirebaseAuth.getInstance().uid!!
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.EnteredName -> {
                _name.value = event.value
            }

            is ProfileEvent.EnteredRegistrationCode -> {
                _registrationCode.value = event.value
            }

            is ProfileEvent.Save -> {
                viewModelScope.launch { save() }
            }

            is ProfileEvent.SignOut -> {
                viewModelScope.launch { signOut() }
            }
        }
    }

    private suspend fun save() {
        participantUseCases.createUseCase(
            input = CreateUseCase.Input(
                registrationCode = registrationCode.value,
                uid = currentUid.value,
                name = name.value
            )
        ).result.let {
            if (it.isSuccess) {
                onEvent(StudyTalkEvent.EnteredRoute(BaseScreens.WaitingApproveScreen.route))
                onEvent(StudyTalkEvent.EnteredUiText(UiText.StringResource(R.string.participant_created_message)))
            } else {
                onEvent(StudyTalkEvent.EnteredExceptionMessage(it.exceptionOrNull()!!.message!!))
            }
        }
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        onEvent(StudyTalkEvent.EnteredRoute(BaseScreens.InitialActivity.route))
    }
}