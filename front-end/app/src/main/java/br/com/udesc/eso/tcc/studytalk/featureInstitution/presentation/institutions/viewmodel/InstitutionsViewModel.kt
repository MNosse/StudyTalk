package br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.institutions.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkAdministratorHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkEvent
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkParticipantHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase.GetAllUseCase
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase.InstitutionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstitutionsViewModel @Inject constructor(
    private val institutionUseCases: InstitutionUseCases,
    studyTalkAdministratorHandler: StudyTalkAdministratorHandler
) : StudyTalkViewModel() {
    private val administratorUid: String

    private val _state = mutableStateOf(InstitutionsState())
    val state: State<InstitutionsState> = _state

    init {
        administratorUid = studyTalkAdministratorHandler.currentAdministrator!!.uid
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
                it.getOrNull()!!.let { institutions ->
                    _state.value = state.value.copy(
                        institutions = institutions.reversed()
                    )
                }
            } else {
                onEvent(StudyTalkEvent.EnteredExceptionMessage(it.exceptionOrNull()!!.message!!))
            }
        }
    }
}