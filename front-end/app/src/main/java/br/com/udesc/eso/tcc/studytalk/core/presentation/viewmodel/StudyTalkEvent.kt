package br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel

sealed class StudyTalkEvent {
    object ClearRoute : StudyTalkEvent()
    data class EnteredRoute(val value: String) : StudyTalkEvent()
}