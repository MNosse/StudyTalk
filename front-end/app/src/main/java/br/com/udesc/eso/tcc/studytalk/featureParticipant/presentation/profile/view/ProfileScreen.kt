package br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.profile.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.initial.InitialScreens
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkFloatingActionButton
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkScreen
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkTextField
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkTopAppBar
import br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.profile.viewmodel.ProfileEvent
import br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.profile.viewmodel.ProfileViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val currentParticipantId = viewModel.currentParticipantId.value
    val isEditMode = viewModel.isEditMode.value
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            StudyTalkTopAppBar(title = stringResource(R.string.profile_top_app_bar_title))
        },
        content = {
            StudyTalkScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                viewModel = viewModel
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    StudyTalkTextField(
                        label = stringResource(R.string.name_label),
                        value = viewModel.name.value,
                        singleLine = true,
                        enabled = (currentParticipantId == -1L || isEditMode),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = {
                            viewModel.onEvent(
                                ProfileEvent.EnteredName(
                                    it
                                )
                            )
                        }
                    )
                    if (currentParticipantId == -1L) {
                        StudyTalkTextField(
                            label = stringResource(R.string.registration_code_label),
                            value = viewModel.registrationCode.value,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                }
                            ),
                            onValueChange = {
                                viewModel.onEvent(
                                    ProfileEvent.EnteredRegistrationCode(
                                        it
                                    )
                                )
                            }
                        )
                    }
                    if (!isEditMode) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ClickableText(
                                text = AnnotatedString(text = "Desconectar"),
                                style = TextStyle(
                                    textDecoration = TextDecoration.Underline,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                onClick = {
                                    viewModel.onEvent(ProfileEvent.SignOut)
                                }
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            if (currentParticipantId == -1L || isEditMode) {
                StudyTalkFloatingActionButton(
                    iconVector = Icons.Filled.Check,
                    iconDescription = stringResource(R.string.participant_fab_content_description),
                    text = stringResource(R.string.add_institution_fab_text),
                    onClick = {
                        keyboardController?.hide()
                        viewModel.onEvent(ProfileEvent.Save)
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    )
}