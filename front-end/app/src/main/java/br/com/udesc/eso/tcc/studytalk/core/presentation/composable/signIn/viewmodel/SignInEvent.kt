package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.signIn.viewmodel

sealed class SignInEvent {
    data class EnteredEmail(val value: String) : SignInEvent()
    data class EnteredPassword(val value: String) : SignInEvent()
    object SignIn : SignInEvent()
}
