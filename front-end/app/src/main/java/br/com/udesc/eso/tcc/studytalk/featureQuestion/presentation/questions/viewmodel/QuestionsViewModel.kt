package br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.questions.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkAdministratorHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkEvent
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkParticipantHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.useCase.GetAllByInstitutionUseCase
import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.useCase.QuestionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val questionUseCases: QuestionUseCases,
    studyTalkAdministratorHandler: StudyTalkAdministratorHandler,
    studyTalkParticipantHandler: StudyTalkParticipantHandler
) : StudyTalkViewModel(studyTalkAdministratorHandler, studyTalkParticipantHandler) {
    private val _currentInstitutionId = mutableLongStateOf(-1L)
    val currentInstitutionId: State<Long> = _currentInstitutionId

    private val _currentUid = mutableStateOf("")
    val currentUid: State<String> = _currentUid

    private val _state = mutableStateOf(QuestionsState())
    val state: State<QuestionsState> = _state

    init {
        val currentParticipant = currentParticipant?.copy()
        _currentInstitutionId.longValue = currentParticipant?.institution!!.id
        _currentUid.value = currentParticipant.uid
        viewModelScope.launch {
            getQuestions()
        }
    }

    private suspend fun getQuestions() {
        questionUseCases.getAllByInstitutionUseCase(
            input = GetAllByInstitutionUseCase.Input(
                id = currentInstitutionId.value,
                participantUid = currentUid.value
            )
        ).result.let { result ->
            if (result.isSuccess) {
                result.getOrNull()!!.let { questions ->
                    _state.value = state.value.copy(
                        questions = questions
                    )
                }
            } else {
                onEvent(StudyTalkEvent.EnteredExceptionMessage(result.exceptionOrNull()!!.message!!))
            }
        }
    }
}