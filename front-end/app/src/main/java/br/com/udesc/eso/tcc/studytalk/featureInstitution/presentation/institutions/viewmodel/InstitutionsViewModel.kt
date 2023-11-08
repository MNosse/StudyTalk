package br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.institutions.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.core.utils.UiText
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase.GetAllUseCase
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase.InstitutionUseCases
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstitutionsViewModel @Inject constructor(
    private val institutionUseCases: InstitutionUseCases,
    sharedPreferences: SharedPreferences
) : StudyTalkViewModel() {

    private var administratorUid: String

    private val _snackbarMessage: MutableState<UiText> = mutableStateOf(UiText.DynamicString(""))
    val snackbarMessage: State<UiText> = _snackbarMessage

    private val _state = mutableStateOf(InstitutionsState())
    val state: State<InstitutionsState> = _state

    init {
        administratorUid = sharedPreferences.getString("current_uid", "")!!

        viewModelScope.launch {
            getInstitutions()
        }
    }

    private suspend fun getInstitutions() {
        institutionUseCases.getAllUseCase(
            input = GetAllUseCase.Input(
                administratorUid = administratorUid
            )
        ).result.let {
            if (it.isSuccess) {
                it.getOrNull()!!.let {
                    _state.value = state.value.copy(
                        institutions = it
                    )
                }
            } else {
                exceptionToSnackbarMessages(it.exceptionOrNull()!!.message!!)
            }
        }
    }

    private fun exceptionToSnackbarMessages(exception: String) {
        val exceptions = Gson().fromJson(
            exception,
            Map::class.java
        )
        var newSnackbarMessage = ""
        var count = 0
        exceptions.forEach { (_, value) ->
            count++
            newSnackbarMessage += (value as String)
            if (count < exceptions.keys.size) {
                newSnackbarMessage += "\n"
            }
        }
        _snackbarMessage.value = UiText.DynamicString(newSnackbarMessage)
    }

}