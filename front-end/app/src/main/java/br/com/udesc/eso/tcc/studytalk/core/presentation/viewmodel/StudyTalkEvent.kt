package br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel

import br.com.udesc.eso.tcc.studytalk.core.utils.UiText

sealed class StudyTalkEvent {
    object ClearMessage : StudyTalkEvent()
    object ClearRoute : StudyTalkEvent()
    data class EnteredExceptionMessage(val value: String) : StudyTalkEvent()
    data class EnteredMessage(val value: String) : StudyTalkEvent()
    data class EnteredUiText(val value: UiText) : StudyTalkEvent()
    data class EnteredRoute(val value: String) : StudyTalkEvent()
}