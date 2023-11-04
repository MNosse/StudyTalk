package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.about

sealed class AboutEvent {
    object ChangeMode : AboutEvent()
    object HideDialog : AboutEvent()
    object LogoClick : AboutEvent()
}
