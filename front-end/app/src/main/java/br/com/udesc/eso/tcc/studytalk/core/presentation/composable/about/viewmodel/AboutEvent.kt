package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.about.viewmodel

sealed class AboutEvent {
    object ChangeMode : AboutEvent()
    object ClearSnackbarMessage : AboutEvent()
}
