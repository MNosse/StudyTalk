package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.about.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.core.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : StudyTalkViewModel() {

    private val _newMode = mutableStateOf("Administrador")
    val newMode: State<String> = _newMode

    private val _snackbarMessage: MutableState<UiText> = mutableStateOf(UiText.DynamicString(""))
    val snackbarMessage: State<UiText> = _snackbarMessage

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
                _snackbarMessage.value = UiText.StringResource(R.string.mode_changed, newMode.value)
                setNewMode(newMode.value)
            }

            is AboutEvent.ClearSnackbarMessage -> {
                _snackbarMessage.value = UiText.DynamicString("")
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