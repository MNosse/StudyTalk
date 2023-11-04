package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.signUp

sealed class SignUpEvent {
    object ClearRoute : SignUpEvent()
    object ClearSnackbarMessage : SignUpEvent()
    data class EnteredEmail(val value: String) : SignUpEvent()
    data class EnteredPassword(val value: String) : SignUpEvent()
    object SignUp : SignUpEvent()
}
