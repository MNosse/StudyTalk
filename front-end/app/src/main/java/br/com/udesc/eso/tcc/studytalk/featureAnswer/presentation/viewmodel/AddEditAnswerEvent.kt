package br.com.udesc.eso.tcc.studytalk.featureAnswer.presentation.viewmodel

sealed class AddEditAnswerEvent {
    data class EnteredDescription(val value: String) : AddEditAnswerEvent()
    object Save : AddEditAnswerEvent()
}