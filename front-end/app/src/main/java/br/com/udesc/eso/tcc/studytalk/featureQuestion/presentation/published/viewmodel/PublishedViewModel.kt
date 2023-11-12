package br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.published.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkEvent
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkParticipantHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.useCase.GetAllByInstitutionUseCase
import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.useCase.QuestionUseCases
import br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.favorited.viewmodel.FavoritedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublishedViewModel @Inject constructor(
    private val questionUseCase: QuestionUseCases,
    studyTalkParticipantHandler: StudyTalkParticipantHandler
) : StudyTalkViewModel() {
    private val currentInstitutionId: Long
    private val currentParticipantUid: String

    private val _state = mutableStateOf(FavoritedState())
    val state: State<FavoritedState> = _state

    init {
        val currentParticipant = studyTalkParticipantHandler.currentParticipant!!.copy()
        currentInstitutionId = currentParticipant.institution!!.id
        currentParticipantUid = currentParticipant.uid
        viewModelScope.launch {
            getQuestions()
        }
    }

    private suspend fun getQuestions() {
        questionUseCase.getAllByInstitutionUseCase(
            input = GetAllByInstitutionUseCase.Input(
                id = currentInstitutionId,
                participantUid = currentParticipantUid
            )
        ).result.let { result ->
            if (result.isSuccess) {
                result.getOrNull()!!.let { questions ->
                    _state.value = state.value.copy(
                        questions = questions.filter { it.participant!!.uid == currentParticipantUid }
                            .reversed()
                    )
                }
            } else {
                onEvent(StudyTalkEvent.EnteredExceptionMessage(result.exceptionOrNull()!!.message!!))
            }
        }
    }
}