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
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkEvent
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkParticipantHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.core.utils.UiText
import br.com.udesc.eso.tcc.studytalk.featureAnswer.domain.useCase.AnswerUseCases
import br.com.udesc.eso.tcc.studytalk.featureAnswer.domain.useCase.GetAllByQuestionUseCase
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.ChangeAQuestionFavoriteStatusUseCase
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.ChangeAnAnswerLikeStatusUseCase
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
    private val answerUseCases: AnswerUseCases,
    private val participantUseCases: ParticipantUseCases,
    private val questionUseCases: QuestionUseCases,
    savedStateHandle: SavedStateHandle,
    studyTalkParticipantHandler: StudyTalkParticipantHandler
) : StudyTalkViewModel() {
    private val _isOwner = mutableStateOf(false)
    val isOwner: State<Boolean> = _isOwner

    private val _currentBackRoute = CurrentBackRoute
    val currentBackRoute: State<String> = _currentBackRoute.route

    private val _currentQuestionId = mutableLongStateOf(-1L)
    val currentQuestionId: State<Long> = _currentQuestionId

    private val _currentParticipantUid = mutableStateOf("")
    val currentParticipantUid: State<String> = _currentParticipantUid

    private val _description = mutableStateOf("")
    val description: State<String> = _description

    private val _editMode = mutableStateOf(false)
    val editMode: State<Boolean> = _editMode

    private val _isFavorited = mutableStateOf(false)
    val isFavorited: State<Boolean> = _isFavorited

    private val _selectedSubjects = mutableStateListOf<Subject>()
    val selectedSubjects: List<Subject> = _selectedSubjects

    private val _state = mutableStateOf(AddEditViewQuestionState())
    val state: State<AddEditViewQuestionState> = _state

    private val _subjects = enumValues<Subject>()
    val subjects = _subjects

    private val _title = mutableStateOf("")
    val title: State<String> = _title

    init {
        val currentParticipant = studyTalkParticipantHandler.currentParticipant!!.copy()
        _currentParticipantUid.value = currentParticipant.uid
        _currentQuestionId.longValue = savedStateHandle.get<Long>("questionId")!!
        savedStateHandle.get<String>("backRoute")!!.let { route ->
            if (route.isNotBlank()) {
                _currentBackRoute.route.value = route
            }
        }

        if (currentQuestionId.value != -1L) {
            runBlocking {
                questionUseCases.getByIdUseCase(
                    input = GetByIdUseCase.Input(
                        id = currentQuestionId.value,
                        participantUid = currentParticipantUid.value,
                    )
                ).result.let {
                    if (it.isSuccess) {
                        it.getOrNull()!!.also { question ->
                            _isOwner.value =
                                question.participant!!.uid == currentParticipantUid.value
                            _title.value = question.title
                            _description.value = question.description
                            _selectedSubjects.clear()
                            _selectedSubjects.addAll(question.subjects)
                        }
                    }
                }
                getAnswers()
            }
        }

        _isFavorited.value =
            currentParticipant.favoriteQuestions.any { it.id == currentQuestionId.value }
        _state.value = state.value.copy(
            likedAnswers = currentParticipant.likedAnswers
        )
    }

    fun onEvent(event: AddEditViewQuestionEvent) {
        when (event) {
            is AddEditViewQuestionEvent.DeleteAnswer -> {
                viewModelScope.launch { deleteAnswer(event.value) }
            }

            is AddEditViewQuestionEvent.DeleteQuestion -> {
                viewModelScope.launch { deleteQuestion() }
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

            is AddEditViewQuestionEvent.LikeAnswer -> {
                viewModelScope.launch { likeAnswer(event.value) }
            }

            is AddEditViewQuestionEvent.LoadState -> {

            }

            is AddEditViewQuestionEvent.Save -> {
                viewModelScope.launch { save() }
            }
        }
    }

    private suspend fun getAnswers() {
        answerUseCases.getAllByQuestionUseCase(
            input = GetAllByQuestionUseCase.Input(
                id = currentQuestionId.value,
                participantUid = currentParticipantUid.value
            )
        ).result.let { result ->
            if (result.isSuccess) {
                result.getOrNull()!!.let { answers ->
                    _state.value = state.value.copy(
                        answers = answers
                    )
                }
            } else {
                onEvent(StudyTalkEvent.EnteredExceptionMessage(result.exceptionOrNull()!!.message!!))
            }
        }
    }

    private suspend fun deleteAnswer(answerId: Long) {
        handleResult(
            result = answerUseCases.deleteUseCase(
                input = br.com.udesc.eso.tcc.studytalk.featureAnswer.domain.useCase.DeleteUseCase.Input(
                    id = answerId,
                    participantUid = currentParticipantUid.value
                )
            ).result,
            message = UiText.StringResource(
                R.string.generic_snackbar_message,
                UiText.StringResource(R.string.answer),
                UiText.StringResource(
                    R.string.removed,
                    UiText.StringResource(R.string.fem_gender)
                )
            )
        )
        val answers = state.value.answers.toMutableList()
        answers.removeIf { it.id == answerId }
        _state.value = state.value.copy(
            answers = answers
        )
    }

    private suspend fun deleteQuestion() {
        handleResult(
            result = questionUseCases.deleteUseCase(
                input = DeleteUseCase.Input(
                    id = currentQuestionId.value,
                    participantUid = currentParticipantUid.value
                )
            ).result,
            route = BaseScreens.QuestionsScreen.route,
            message = UiText.StringResource(
                R.string.generic_snackbar_message,
                UiText.StringResource(R.string.question),
                UiText.StringResource(
                    R.string.removed,
                    UiText.StringResource(R.string.fem_gender)
                )
            )
        )
    }

    private suspend fun handleFavorite() {
        handleResult(
            result = participantUseCases.changeAQuestionFavoriteStatusUseCase(
                input = ChangeAQuestionFavoriteStatusUseCase.Input(
                    participantUid = currentParticipantUid.value,
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

    private suspend fun likeAnswer(answerId: Long) {
        val likedAnswers = state.value.likedAnswers.toMutableList()
        val message: UiText
        likedAnswers.find { answer -> answer.id == answerId }.let { answer ->
            if (answer != null) {
                likedAnswers.remove(answer)
                message = UiText.StringResource(
                    R.string.generic_snackbar_message,
                    UiText.StringResource(R.string.like),
                    UiText.StringResource(
                        R.string.removed,
                        UiText.StringResource(R.string.fem_gender)
                    )
                )
            } else {
                likedAnswers.add(state.value.answers.first { it.id == answerId })
                message = UiText.StringResource(
                    R.string.generic_snackbar_message,
                    UiText.StringResource(R.string.like),
                    UiText.StringResource(
                        R.string.added,
                        UiText.StringResource(R.string.fem_gender)
                    )
                )
            }
        }
        handleResult(
            result = participantUseCases.changeAnAnswerLikeStatusUseCase(
                input = ChangeAnAnswerLikeStatusUseCase.Input(
                    participantUid = currentParticipantUid.value,
                    answerId = answerId
                )
            ).result,
            message = message
        )
        _state.value = state.value.copy(
            likedAnswers = likedAnswers
        )
    }

    private suspend fun save() {
        if (currentQuestionId.value == -1L) {
            handleResult(
                result = participantUseCases.doAQuestionUseCase(
                    input = DoAQuestionUseCase.Input(
                        participantUid = currentParticipantUid.value,
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
                        participantUid = currentParticipantUid.value
                    )
                ).result,
                route = BaseScreens.QuestionsScreen.route,
                message = UiText.StringResource(R.string.question_updated_message)
            )
        }
    }
}

object CurrentBackRoute {
    var route = mutableStateOf(BaseScreens.QuestionsScreen.route)
}