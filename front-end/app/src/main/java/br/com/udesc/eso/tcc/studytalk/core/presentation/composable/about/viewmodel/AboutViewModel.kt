package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.about.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkAdministratorHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkEvent
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkParticipantHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.core.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    studyTalkAdministratorHandler: StudyTalkAdministratorHandler,
    studyTalkParticipantHandler: StudyTalkParticipantHandler
) : StudyTalkViewModel(studyTalkAdministratorHandler, studyTalkParticipantHandler) {

    private val _newMode = mutableStateOf("Administrador")
    val newMode: State<String> = _newMode

    init {
        val currentMode = when (sharedPreferences.getString("current_mode", "participant")) {
            "administrator" -> "Administrador"
            else -> "Participante"
        }
        setNewMode(currentMode)
    }

    fun onEvent(event: AboutEvent) {
        when (event) {
            is AboutEvent.ChangeMode -> {
                onEvent(
                    StudyTalkEvent.EnteredUiText(
                        UiText.StringResource(
                            R.string.mode_changed,
                            newMode.value
                        )
                    )
                )
                setNewMode(newMode.value)
            }
        }
    }

    private fun setNewMode(currentMode: String) {
        when (currentMode) {
            "Administrador" -> {
                sharedPreferences.edit().putString("current_mode", "administrator").apply()
                _newMode.value = "Participante"
            }

            else -> {
                sharedPreferences.edit().putString("current_mode", "participant").apply()
                _newMode.value = "Administrador"
            }
        }

    }
}