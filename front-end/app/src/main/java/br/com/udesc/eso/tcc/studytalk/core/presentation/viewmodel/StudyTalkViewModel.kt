package br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import br.com.udesc.eso.tcc.studytalk.core.utils.UiText
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.model.Administrator
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.model.Participant
import com.google.gson.Gson

open class StudyTalkViewModel : ViewModel() {
    private val _messageHandler = StudyTalkMessageHandler
    val message: State<UiText> = _messageHandler.message

    private val _route = mutableStateOf("")
    val route: State<String> = _route

    fun onEvent(event: StudyTalkEvent) {
        when (event) {
            is StudyTalkEvent.ClearMessage -> {
                _messageHandler.message.value = UiText.DynamicString("")
            }

            is StudyTalkEvent.ClearRoute -> {
                _route.value = ""
            }

            is StudyTalkEvent.EnteredExceptionMessage -> {
                _messageHandler.message.value = UiText.DynamicString(
                    Gson().fromJson(
                        event.value,
                        Map::class.java
                    ).values.firstOrNull() as String
                )
            }

            is StudyTalkEvent.EnteredMessage -> {
                _messageHandler.message.value = UiText.DynamicString(event.value)
            }

            is StudyTalkEvent.EnteredUiText -> {
                _messageHandler.message.value = event.value
            }

            is StudyTalkEvent.EnteredRoute -> {
                _route.value = event.value
            }
        }
    }

    fun handleResult(result: Result<Unit>, route: String = "", message: UiText) {
        if (result.isSuccess) {
            onEvent(StudyTalkEvent.EnteredRoute(route))
            onEvent(StudyTalkEvent.EnteredUiText(message))
        } else {
            onEvent(StudyTalkEvent.EnteredExceptionMessage(result.exceptionOrNull()!!.message!!))
        }
    }
}