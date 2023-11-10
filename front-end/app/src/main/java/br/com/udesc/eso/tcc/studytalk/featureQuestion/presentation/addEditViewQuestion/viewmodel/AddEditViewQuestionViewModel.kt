package br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.addEditViewQuestion.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.domain.model.Subject
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.base.BaseScreens
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkAdministratorHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkParticipantHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.core.utils.UiText
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.ChangeAQuestionFavoriteStatusUseCase
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.DoAQuestionUseCase
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.ParticipantUseCases
import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.useCase.DeleteUseCase
import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.useCase.GetByIdUseCase
import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.useCase.QuestionUseCases
import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.useCase.UpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AddEditViewQuestionViewModel @Inject constructor(
    private val participantUseCases: ParticipantUseCases,
    private val questionUseCases: QuestionUseCases,
    savedStateHandle: SavedStateHandle,
    studyTalkAdministratorHandler: StudyTalkAdministratorHandler,
    studyTalkParticipantHandler: StudyTalkParticipantHandler
) : StudyTalkViewModel(studyTalkAdministratorHandler, studyTalkParticipantHandler) {
    private val _isOwner = mutableStateOf(false)
    val isOwner: State<Boolean> = _isOwner

    private val _currentQuestionId = mutableLongStateOf(-1L)
    val currentQuestionId: State<Long> = _currentQuestionId

    private val _currentUid = mutableStateOf("")
    val currentUid: State<String> = _currentUid

    private val _description = mutableStateOf("")
    val description: State<String> = _description

    private val _editMode = mutableStateOf(false)
    val editMode: State<Boolean> = _editMode

    private val _isFavorited = mutableStateOf(false)
    val isFavorited: State<Boolean> = _isFavorited

    private val _selectedSubjects = mutableStateListOf<Subject>()
    val selectedSubjects: List<Subject> = _selectedSubjects

    private val _subjects = enumValues<Subject>()
    val subjects = _subjects

    private val _title = mutableStateOf("")
    val title: State<String> = _title

    init {
        val currentParticipant = currentParticipant?.copy()
        _currentUid.value = currentParticipant!!.uid

        savedStateHandle.get<Long>("questionId")?.let { questionId ->
            if (questionId != -1L) {
                runBlocking {
                    questionUseCases.getByIdUseCase(
                        input = GetByIdUseCase.Input(
                            id = questionId,
                            participantUid = currentUid.value,
                        )
                    ).result.let {
                        if (it.isSuccess) {
                            it.getOrNull()!!.also { question ->
                                _isOwner.value = question.participant!!.uid == currentUid.value
                                _currentQuestionId.longValue = question.id
                                _title.value = question.title
                                _description.value = question.description
                                _selectedSubjects.clear()
                                _selectedSubjects.addAll(question.subjects)
                            }
                        }
                    }
                }
            }
        }

        _isFavorited.value = currentParticipant.favoriteQuestions.any { it.id == _currentQuestionId.longValue }
    }

    fun onEvent(event: AddEditViewQuestionEvent) {
        when (event) {
            is AddEditViewQuestionEvent.Delete -> {
                viewModelScope.launch { delete() }
            }

            is AddEditViewQuestionEvent.EnteredDescription -> {
                _description.value = event.value
            }

            is AddEditViewQuestionEvent.EnteredEditMode -> {
                _editMode.value = event.value
            }

            is AddEditViewQuestionEvent.EnteredTitle -> {
                _title.value = event.value
            }

            is AddEditViewQuestionEvent.HandleFavorite -> {
                viewModelScope.launch { handleFavorite() }
            }

            is AddEditViewQuestionEvent.HandleSubject -> {
                _selectedSubjects.removeIf { it == event.value }.let { removed ->
                    if (!removed) {
                        _selectedSubjects.add(event.value)
                    }
                }
            }

            is AddEditViewQuestionEvent.Save -> {
                viewModelScope.launch { save() }
            }
        }
    }

    private suspend fun delete() {
        handleResult(
            result = questionUseCases.deleteUseCase(
                input = DeleteUseCase.Input(
                    id = currentQuestionId.value,
                    participantUid = currentUid.value
                )
            ).result,
            route = BaseScreens.QuestionsScreen.route,
            message = UiText.StringResource(R.string.question_deleted_message)
        )
    }

    private suspend fun handleFavorite() {
        handleResult(
            result = participantUseCases.changeAQuestionFavoriteStatusUseCase(
                input = ChangeAQuestionFavoriteStatusUseCase.Input(
                    participantUid = currentUid.value,
                    questionId = currentQuestionId.value
                )
            ).result,
            message = UiText.StringResource(
                R.string.generic_snackbar_message,
                UiText.StringResource(R.string.favorite),
                if (isFavorited.value) {
                    UiText.StringResource(
                        R.string.removed,
                        UiText.StringResource(R.string.masc_gender)
                    )
                } else {
                    UiText.StringResource(
                        R.string.added,
                        UiText.StringResource(R.string.masc_gender)
                    )
                }
            )
        )
        _isFavorited.value = !isFavorited.value
    }

    private suspend fun save() {
        if (currentQuestionId.value == -1L) {
            handleResult(
                result = participantUseCases.doAQuestionUseCase(
                    input = DoAQuestionUseCase.Input(
                        participantUid = currentUid.value,
                        title = title.value,
                        description = description.value,
                        subjects = selectedSubjects
                    )
                ).result,
                route = BaseScreens.QuestionsScreen.route,
                message = UiText.StringResource(R.string.question_created_message)
            )
        } else {
            handleResult(
                result = questionUseCases.updateUseCase(
                    input = UpdateUseCase.Input(
                        id = currentQuestionId.value,
                        title = title.value.let { it.ifBlank { null } },
                        description = description.value.let { it.ifBlank { null } },
                        subjects = selectedSubjects,
                        participantUid = currentUid.value
                    )
                ).result,
                route = BaseScreens.QuestionsScreen.route,
                message = UiText.StringResource(R.string.question_updated_message)
            )
        }
    }
}