package br.com.udesc.eso.tcc.studytalk.core.presentation.activity.base

sealed class BaseEvent {
    data class ChangeCurrentIndex(val index: Int) : BaseEvent()
    object SignOut : BaseEvent()
}