package br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.create.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.base.BaseScreens
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkAdministratorHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkEvent
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkParticipantHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.core.utils.UiText
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.CreateUseCase
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.ParticipantUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateParticipantViewModel @Inject constructor(
    private val participantUseCases: ParticipantUseCases,
    sharedPreferences: SharedPreferences,
    studyTalkAdministratorHandler: StudyTalkAdministratorHandler,
    studyTalkParticipantHandler: StudyTalkParticipantHandler
) : StudyTalkViewModel(studyTalkAdministratorHandler, studyTalkParticipantHandler) {

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _participantUid = mutableStateOf("")
    val participantUid: State<String> = _participantUid

    private val _registrationCode = mutableStateOf("")
    val registrationCode: State<String> = _registrationCode

    init {
        _participantUid.value = sharedPreferences.getString("current_uid", "")!!
    }

    fun onEvent(event: CreateParticipantEvent) {
        when (event) {
            is CreateParticipantEvent.EnteredName -> {
                _name.value = event.value
            }

            is CreateParticipantEvent.EnteredRegistrationCode -> {
                _registrationCode.value = event.value
            }

            is CreateParticipantEvent.Save -> {
                viewModelScope.launch { save() }
            }
        }
    }

    private suspend fun save() {
        participantUseCases.createUseCase(
            input = CreateUseCase.Input(
                registrationCode = registrationCode.value,
                uid = participantUid.value,
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
}