package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.presentation.enrollmentRequesties.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkEvent
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkParticipantHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.core.utils.UiText
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.useCase.ApproveUseCase
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.useCase.EnrollmentRequestUseCases
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.useCase.GetAllByInstitutionUseCase
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.useCase.ReproveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnrollmentRequestiesViewModel @Inject constructor(
    private val enrollmentRequestUseCases: EnrollmentRequestUseCases,
    private val studyTalkParticipantHandler: StudyTalkParticipantHandler
) : StudyTalkViewModel() {
    private val _currentInstitutionId = mutableLongStateOf(-1L)
    val currentInstitutionId: State<Long> = _currentInstitutionId

    private val _currentUid = mutableStateOf("")
    val currentUid: State<String> = _currentUid

    private val _state = mutableStateOf(EnrollmentRequestiesState())
    val state: State<EnrollmentRequestiesState> = _state

    init {
        val currentParticipant = studyTalkParticipantHandler.currentParticipant?.copy()
        _currentInstitutionId.longValue = currentParticipant?.institution!!.id
        _currentUid.value = currentParticipant.uid
        viewModelScope.launch {
            getEnrollmentRequesties()
        }
    }

    fun onEvent(event: EnrollmentRequestiesEvent) {
        when (event) {
            is EnrollmentRequestiesEvent.Approve -> {
                viewModelScope.launch { approve(event.value) }
            }

            is EnrollmentRequestiesEvent.Reprove -> {
                viewModelScope.launch { reprove(event.value) }
            }
        }
    }

    private suspend fun getEnrollmentRequesties() {
        enrollmentRequestUseCases.getAllByInstitutionUseCase(
            input = GetAllByInstitutionUseCase.Input(
                id = currentInstitutionId.value,
                requestingParticipantUid = currentUid.value
            )
        ).result.let { result ->
            if (result.isSuccess) {
                result.getOrNull()!!.let { enrollmentRequesties ->
                    _state.value = state.value.copy(
                        enrollmentRequesties = enrollmentRequesties
                    )
                }
            } else {
                onEvent(StudyTalkEvent.EnteredExceptionMessage(result.exceptionOrNull()!!.message!!))
            }
        }
    }

    private suspend fun approve(enrollmentRequestId: Long) {
        handleResult(
            enrollmentRequestUseCases.approveUseCase(
                input = ApproveUseCase.Input(
                    id = enrollmentRequestId,
                    approverUid = currentUid.value
                )
            ).result,
            message = UiText.StringResource(
                R.string.generic_snackbar_message,
                UiText.StringResource(R.string.enrollment_request),
                UiText.StringResource(
                    R.string.approved,
                    UiText.StringResource(R.string.fem_gender)
                )
            )
        )
        _state.value = state.value.copy(
            enrollmentRequesties = state.value.enrollmentRequesties.filter { it.id != enrollmentRequestId }
        )
    }

    private suspend fun reprove(enrollmentRequestId: Long) {
        handleResult(
            enrollmentRequestUseCases.reproveUseCase(
                input = ReproveUseCase.Input(
                    id = enrollmentRequestId,
                    reproverUid = currentUid.value
                )
            ).result,
            message = UiText.StringResource(
                R.string.generic_snackbar_message,
                UiText.StringResource(R.string.enrollment_request),
                UiText.StringResource(
                    R.string.reproved,
                    UiText.StringResource(R.string.fem_gender)
                )
            )
        )
        _state.value = state.value.copy(
            enrollmentRequesties = state.value.enrollmentRequesties.filter { it.id != enrollmentRequestId }
        )
    }
}