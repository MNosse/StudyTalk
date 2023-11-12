package br.com.udesc.eso.tcc.studytalk.featureAnswer.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.base.BaseScreens
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkAdministratorHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkParticipantHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.core.utils.UiText
import br.com.udesc.eso.tcc.studytalk.featureAnswer.domain.useCase.AnswerUseCases
import br.com.udesc.eso.tcc.studytalk.featureAnswer.domain.useCase.GetByIdUseCase
import br.com.udesc.eso.tcc.studytalk.featureAnswer.domain.useCase.UpdateUseCase
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.AnswerAQuestionUseCase
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.ParticipantUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AddEditAnswerViewModel @Inject constructor(
    private val answerUseCases: AnswerUseCases,
    private val participantUseCases: ParticipantUseCases,
    savedStateHandle: SavedStateHandle,
    studyTalkAdministratorHandler: StudyTalkAdministratorHandler,
    studyTalkParticipantHandler: StudyTalkParticipantHandler,
) : StudyTalkViewModel(studyTalkAdministratorHandler, studyTalkParticipantHandler) {
    private val currentAnswerId: Long

    private val currentQuestionId: Long

    private val currentUid: String

    private val _description = mutableStateOf("")
    val description: State<String> = _description

    private val _editMode = mutableStateOf(false)
    val editMode: State<Boolean> = _editMode

    init {
        val currentParticipant = currentParticipant?.copy()
        currentUid = currentParticipant!!.uid

        currentQuestionId = savedStateHandle.get<Long>("questionId")!!

        currentAnswerId = savedStateHandle.get<Long>("answerId")!!
        if (currentAnswerId != -1L) {
            _editMode.value = true
            runBlocking {
                answerUseCases.getByIdUseCase(
                    input = GetByIdUseCase.Input(
                        id = currentAnswerId,
                        participantUid = currentUid,
                    )
                ).result.let {
                    if (it.isSuccess) {
                        it.getOrNull()!!.also { answer ->
                            _description.value = answer.description
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditAnswerEvent) {
        when (event) {
            is AddEditAnswerEvent.EnteredDescription -> {
                _description.value = event.value
            }

            is AddEditAnswerEvent.Save -> {
                runBlocking { save() }
            }
        }
    }

    private suspend fun save() {
        if (currentAnswerId == -1L) {
            handleResult(
                result = participantUseCases.answerAQuestionUseCase(
                    input = AnswerAQuestionUseCase.Input(
                        participantUid = currentUid,
                        questionId = currentQuestionId,
                        description = description.value
                    )
                ).result,
                route = BaseScreens.AddEditViewQuestionScreen.route + "?questionId=${currentQuestionId}",
                message = UiText.StringResource(
                    R.string.generic_snackbar_message,
                    UiText.StringResource(R.string.answer),
                    UiText.StringResource(
                        R.string.created,
                        UiText.StringResource(R.string.fem_gender)
                    )
                )
            )
        } else {
            handleResult(
                result = answerUseCases.updateUseCase(
                    input = UpdateUseCase.Input(
                        id = currentAnswerId,
                        description = description.value,
                        participantUid = currentUid
                    )
                ).result,
                route = BaseScreens.AddEditViewQuestionScreen.route + "?questionId=${currentQuestionId}",
                message = UiText.StringResource(
                    R.string.generic_snackbar_message,
                    UiText.StringResource(R.string.answer),
                    UiText.StringResource(
                        R.string.updated,
                        UiText.StringResource(R.string.fem_gender)
                    )
                )
            )
        }
    }
}