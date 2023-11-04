package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.signIn

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.useCase.AdministratorUseCases
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase.ParticipantUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val administratorUseCases: AdministratorUseCases,
    private val participantUseCases: ParticipantUseCases,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _snackbarMessage = mutableStateOf("")
    val snackbarMessage: State<String> = _snackbarMessage

    private val _route = mutableStateOf("")
    val route: State<String> = _route

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.ClearRoute -> {
                _route.value = ""
            }

            is SignInEvent.ClearSnackbarMessage -> {
                _snackbarMessage.value = ""
            }

            is SignInEvent.EnteredEmail -> {
                _email.value = event.value
            }

            is SignInEvent.EnteredPassword -> {
                _password.value = event.value
            }

            is SignInEvent.SignIn -> {

            }
        }
    }
}