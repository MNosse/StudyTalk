package br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.favorited.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkParticipantHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritedViewModel @Inject constructor(
    private val studyTalkParticipantHandler: StudyTalkParticipantHandler
) : StudyTalkViewModel() {
    private val _state = mutableStateOf(FavoritedState())
    val state: State<FavoritedState> = _state

    init {
        reload()
    }

    fun reload() {
        val currentParticipant = studyTalkParticipantHandler.currentParticipant!!.copy()
        _state.value = state.value.copy(
            questions = currentParticipant!!.favoriteQuestions.reversed()
        )
    }
}