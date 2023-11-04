package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.signIn

sealed class SignInEvent {
    object ClearRoute : SignInEvent()
    object ClearSnackbarMessage : SignInEvent()
    data class EnteredEmail(val value: String) : SignInEvent()
    data class EnteredPassword(val value: String) : SignInEvent()
    object SignIn : SignInEvent()
}
