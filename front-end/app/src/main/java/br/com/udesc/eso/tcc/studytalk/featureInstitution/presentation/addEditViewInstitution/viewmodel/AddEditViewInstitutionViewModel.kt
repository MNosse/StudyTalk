package br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.addEditViewInstitution.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.base.BaseScreens
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkEvent
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.core.utils.UiText
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase.CreateUseCase
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase.DeleteUseCase
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase.GetByIdUseCase
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase.InstitutionUseCases
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase.UpdateUseCase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewInstitutionViewModel @Inject constructor(
    private val institutionUseCases: InstitutionUseCases,
    savedStateHandle: SavedStateHandle,
    sharedPreferences: SharedPreferences
) : StudyTalkViewModel() {

    private val _administratorUid = mutableStateOf("")
    val administratorUid: State<String> = _administratorUid

    private val _currentInstitutionId = mutableLongStateOf(-1L)
    val currentInstitutionId: State<Long> = _currentInstitutionId

    private val _editMode = mutableStateOf(false)
    val editMode: State<Boolean> = _editMode

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _registrationCode = mutableStateOf("")
    val registrationCode: State<String> = _registrationCode

    private val _snackbarMessage: MutableState<UiText> = mutableStateOf(UiText.DynamicString(""))
    val snackbarMessage: State<UiText> = _snackbarMessage

    init {
        _administratorUid.value = sharedPreferences.getString("current_uid", "")!!

        savedStateHandle.get<Long>("institutionId")?.let { institutionId ->
            if (institutionId != -1L) {
                viewModelScope.launch {
                    institutionUseCases.getByIdUseCase(
                        input = GetByIdUseCase.Input(
                            id = institutionId,
                            requestingUid = administratorUid.value,
                            isAdministrator = true
                        )
                    ).result.let {
                        if (it.isSuccess) {
                            it.getOrNull()!!.also { institution ->
                                _currentInstitutionId.longValue = institution.id
                                _name.value = institution.name
                                _registrationCode.value = institution.registrationCode!!
                            }
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditViewInstitutionEvent) {
        when (event) {
            is AddEditViewInstitutionEvent.ChangeEditMode -> {
                _editMode.value = event.value
            }

            is AddEditViewInstitutionEvent.ClearSnackbarMessage -> {
                _snackbarMessage.value = UiText.DynamicString("")
            }

            is AddEditViewInstitutionEvent.Delete -> {
                viewModelScope.launch { delete() }
            }

            is AddEditViewInstitutionEvent.EnteredName -> {
                _name.value = event.value
            }

            is AddEditViewInstitutionEvent.Save -> {
                viewModelScope.launch { save() }
            }
        }
    }

    private suspend fun delete() {
        institutionUseCases.deleteUseCase(
            input = DeleteUseCase.Input(
                id = currentInstitutionId.value,
                administratorUid = administratorUid.value
            )
        ).result.let {
            if (it.isSuccess) {
                _snackbarMessage.value =
                    UiText.StringResource(R.string.institution_deleted_message)
                onEvent(StudyTalkEvent.EnteredRoute(BaseScreens.InstitutionsScreen.route))
            } else {
                exceptionToSnackbarMessages(it.exceptionOrNull()!!.message!!)
            }
        }
    }

    private suspend fun save() {
        if (currentInstitutionId.value == -1L) {
            institutionUseCases.createUseCase(
                input = CreateUseCase.Input(
                    administratorUid = _administratorUid.value,
                    name = name.value
                )
            ).result.let {
                if (it.isSuccess) {
                    _snackbarMessage.value =
                        UiText.StringResource(R.string.institution_created_message)
                    onEvent(StudyTalkEvent.EnteredRoute(BaseScreens.InstitutionsScreen.route))
                } else {
                    exceptionToSnackbarMessages(it.exceptionOrNull()!!.message!!)
                }
            }
        } else {
            institutionUseCases.updateUseCase(
                input = UpdateUseCase.Input(
                    id = currentInstitutionId.value,
                    administratorUid = _administratorUid.value,
                    name = name.value
                )
            ).result.let {
                if (it.isSuccess) {
                    _snackbarMessage.value =
                        UiText.StringResource(R.string.institution_updated_message)
                    onEvent(StudyTalkEvent.EnteredRoute(BaseScreens.InstitutionsScreen.route))
                } else {
                    exceptionToSnackbarMessages(it.exceptionOrNull()!!.message!!)
                }
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