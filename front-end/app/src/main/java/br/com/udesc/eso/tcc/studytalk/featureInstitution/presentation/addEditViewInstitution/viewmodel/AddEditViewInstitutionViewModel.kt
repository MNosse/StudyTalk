package br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.addEditViewInstitution.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.base.BaseScreens
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkAdministratorHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkParticipantHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.core.utils.UiText
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase.CreateUseCase
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase.DeleteUseCase
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase.GetByIdUseCase
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase.InstitutionUseCases
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase.UpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewInstitutionViewModel @Inject constructor(
    private val institutionUseCases: InstitutionUseCases,
    savedStateHandle: SavedStateHandle,
    sharedPreferences: SharedPreferences,
    studyTalkAdministratorHandler: StudyTalkAdministratorHandler,
    studyTalkParticipantHandler: StudyTalkParticipantHandler
) : StudyTalkViewModel(studyTalkAdministratorHandler, studyTalkParticipantHandler) {

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
        handleResult(
            result = institutionUseCases.deleteUseCase(
                input = DeleteUseCase.Input(
                    id = currentInstitutionId.value,
                    administratorUid = administratorUid.value
                )
            ).result,
            route = BaseScreens.InstitutionsScreen.route,
            message = UiText.StringResource(R.string.institution_deleted_message)
        )
    }

    private suspend fun save() {
        if (currentInstitutionId.value == -1L) {
            handleResult(
                result = institutionUseCases.createUseCase(
                    input = CreateUseCase.Input(
                        administratorUid = _administratorUid.value,
                        name = name.value
                    )
                ).result,
                route = BaseScreens.InstitutionsScreen.route,
                message = UiText.StringResource(R.string.institution_created_message)
            )
        } else {
            handleResult(
                result = institutionUseCases.updateUseCase(
                    input = UpdateUseCase.Input(
                        id = currentInstitutionId.value,
                        administratorUid = _administratorUid.value,
                        name = name.value
                    )
                ).result,
                route = BaseScreens.InstitutionsScreen.route,
                message = UiText.StringResource(R.string.institution_updated_message)
            )
        }
    }
}