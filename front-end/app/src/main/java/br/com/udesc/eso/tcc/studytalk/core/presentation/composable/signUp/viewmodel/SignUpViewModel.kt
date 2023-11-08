package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.signUp.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.initial.InitialScreens
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkEvent
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import br.com.udesc.eso.tcc.studytalk.core.utils.UiText
import br.com.udesc.eso.tcc.studytalk.core.utils.localizeFirebaseAuthErrorMessages
import br.com.udesc.eso.tcc.studytalk.core.utils.localizeFirebaseErrorMessages
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.useCase.AdministratorUseCases
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val administratorUseCases: AdministratorUseCases,
    private val sharedPreferences: SharedPreferences
) : StudyTalkViewModel() {

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _snackbarMessage: MutableState<UiText> = mutableStateOf(UiText.DynamicString(""))
    val snackbarMessage: State<UiText> = _snackbarMessage

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.ClearSnackbarMessage -> {
                _snackbarMessage.value = UiText.DynamicString("")
            }

            is SignUpEvent.EnteredEmail -> {
                _email.value = event.value
            }

            is SignUpEvent.EnteredPassword -> {
                _password.value = event.value
            }

            is SignUpEvent.SignUp -> {
                signUp()
            }
        }
    }

    private fun signUp() {
        if (email.value.isNotBlank()) {
            if (password.value.length >= 8) {
                val auth = FirebaseAuth.getInstance()
                auth.useAppLanguage()
                auth.setLanguageCode("pt-BR")
                auth.createUserWithEmailAndPassword(email.value, password.value)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            when (sharedPreferences.getString("current_mode", "participant")) {
                                "administrator" -> {
                                    runBlocking {
                                        val user = auth.currentUser
                                        administratorUseCases.createUseCase(
                                            br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.useCase.CreateUseCase.Input(
                                                uid = user!!.uid
                                            )
                                        ).result.let {
                                            if (it.isSuccess) {
                                                sharedPreferences.edit()
                                                    .putString("current_uid", user.uid).apply()
                                                _snackbarMessage.value =
                                                    UiText.StringResource(R.string.administrator_created_message)
                                                onEvent(StudyTalkEvent.EnteredRoute(InitialScreens.BaseActivity.route))
                                            } else {
                                                user.delete().addOnCompleteListener {
                                                    _snackbarMessage.value =
                                                        UiText.StringResource(R.string.administrator_creation_failed)
                                                }.addOnFailureListener() {
                                                    _snackbarMessage.value = UiText.DynamicString(
                                                        localizeFirebaseAuthErrorMessages((it as FirebaseAuthException).errorCode)
                                                            ?: it.message!!
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                else -> {
                                    val user = auth.currentUser
                                    sharedPreferences.edit().putString("current_uid", user!!.uid)
                                        .apply()
                                    _snackbarMessage.value =
                                        UiText.StringResource(R.string.participant_created_message)
                                    onEvent(StudyTalkEvent.EnteredRoute(InitialScreens.BaseActivity.route))
                                }
                            }
                        }
                    }.addOnFailureListener {
                        _snackbarMessage.value = if (it is FirebaseAuthException) {
                            UiText.DynamicString(
                                localizeFirebaseAuthErrorMessages((it).errorCode)
                                    ?: it.localizedMessage!!
                            )
                        } else if (it is FirebaseException) {
                            UiText.DynamicString(
                                localizeFirebaseErrorMessages((it))
                                    ?: it.localizedMessage!!
                            )
                        } else {
                            UiText.DynamicString(it.localizedMessage!!)
                        }
                    }
            } else {
                _snackbarMessage.value = UiText.StringResource(R.string.invalid_password_length)
            }
        } else {
            _snackbarMessage.value =
                UiText.DynamicString(localizeFirebaseAuthErrorMessages("ERROR_INVALID_EMAIL")!!)
        }
    }
}