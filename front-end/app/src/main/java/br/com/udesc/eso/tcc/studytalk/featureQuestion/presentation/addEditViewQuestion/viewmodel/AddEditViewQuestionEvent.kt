package br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.addEditViewQuestion.viewmodel

import br.com.udesc.eso.tcc.studytalk.core.domain.model.Subject

sealed class AddEditViewQuestionEvent {
    object Delete : AddEditViewQuestionEvent()
    data class EnteredDescription(val value: String) : AddEditViewQuestionEvent()
    data class EnteredEditMode(val value: Boolean) : AddEditViewQuestionEvent()
    data class EnteredTitle(val value: String) : AddEditViewQuestionEvent()
    object HandleFavorite : AddEditViewQuestionEvent()
    data class HandleSubject(val value: Subject) : AddEditViewQuestionEvent()
    object Save : AddEditViewQuestionEvent()
}