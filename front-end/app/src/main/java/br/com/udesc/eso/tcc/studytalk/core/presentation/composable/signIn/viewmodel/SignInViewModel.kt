package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.signIn.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.initial.InitialScreens
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkAdministratorHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkEvent
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkParticipantHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.core.utils.UiText
import br.com.udesc.eso.tcc.studytalk.core.utils.localizeFirebaseAuthErrorMessages
import br.com.udesc.eso.tcc.studytalk.core.utils.localizeFirebaseErrorMessages
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    studyTalkAdministratorHandler: StudyTalkAdministratorHandler,
    studyTalkParticipantHandler: StudyTalkParticipantHandler
) : StudyTalkViewModel(studyTalkAdministratorHandler, studyTalkParticipantHandler) {

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.EnteredEmail -> {
                _email.value = event.value
            }

            is SignInEvent.EnteredPassword -> {
                _password.value = event.value
            }

            is SignInEvent.SignIn -> {
                signIn()
            }
        }
    }

    private fun signIn() {
        if (email.value.isNotBlank()) {
            if (password.value.length >= 8) {
                val auth = FirebaseAuth.getInstance()
                auth.signInWithEmailAndPassword(email.value, password.value)
                    .addOnSuccessListener {
                        val user = auth.currentUser
                        sharedPreferences.edit().putString("current_uid", user!!.uid).apply()
                        onEvent(StudyTalkEvent.EnteredRoute(InitialScreens.BaseActivity.route))
                    }.addOnFailureListener {
                        onEvent(
                            StudyTalkEvent.EnteredMessage(
                                if (it is FirebaseAuthException) {
                                    localizeFirebaseAuthErrorMessages((it).errorCode)
                                        ?: it.localizedMessage!!

                                } else if (it is FirebaseException) {
                                    localizeFirebaseErrorMessages((it))
                                        ?: it.localizedMessage!!
                                } else {
                                    it.localizedMessage!!
                                }
                            )
                        )
                    }

            } else {
                onEvent(StudyTalkEvent.EnteredUiText(UiText.StringResource(R.string.invalid_password_length)))
            }
        } else {
            onEvent(StudyTalkEvent.EnteredMessage(localizeFirebaseAuthErrorMessages("ERROR_INVALID_EMAIL")!!))
        }
    }
}