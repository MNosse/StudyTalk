package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.about

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _logoClickCount = mutableIntStateOf(0)
    val logoClickCount: State<Int> = _logoClickCount

    private val _showChangeModeDialog = mutableStateOf(false)
    val showChangeModeDialog: State<Boolean> = _showChangeModeDialog

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
                setNewMode(newMode.value)
                _showChangeModeDialog.value = false
            }

            is AboutEvent.HideDialog -> {
                _showChangeModeDialog.value = false
            }

            is AboutEvent.LogoClick -> {
                _logoClickCount.intValue = logoClickCount.value + 1
                if (logoClickCount.value >= 5) {
                    _logoClickCount.intValue = 0
                    _showChangeModeDialog.value = !showChangeModeDialog.value
                }
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