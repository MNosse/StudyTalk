package br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.create.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkFloatingActionButton
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkScreen
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkTextField
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkTopAppBar
import br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.create.viewmodel.CreateParticipantEvent
import br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.create.viewmodel.CreateParticipantViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateParticipantScreen(
    navController: NavController,
    viewModel: CreateParticipantViewModel = hiltViewModel()
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage = viewModel.snackbarMessage.value.asString()

    if (snackbarMessage.isNotBlank()) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = String(snackbarMessage.toByteArray()),
                duration = SnackbarDuration.Short
            )
            viewModel.onEvent(CreateParticipantEvent.ClearSnackbarMessage)
        }
    }

    Scaffold(
        topBar = {
            StudyTalkTopAppBar(title = stringResource(R.string.profile_top_app_bar_title))
        },
        content = {
            StudyTalkScreen(
                navController = navController,
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
                        label = stringResource(R.string.add_edit_view_institution_name_label),
                        value = viewModel.name.value,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = {
                            viewModel.onEvent(
                                CreateParticipantEvent.EnteredName(
                                    it
                                )
                            )
                        }
                    )
                    StudyTalkTextField(
                        label = stringResource(R.string.add_edit_view_institution_registration_code_label),
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
                                CreateParticipantEvent.EnteredRegistrationCode(
                                    it
                                )
                            )
                        },
                        removeSpacer = true
                    )
                }
            }
        },
        floatingActionButton = {
            StudyTalkFloatingActionButton(
                iconVector = Icons.Filled.Check,
                iconDescription = stringResource(R.string.participant_fab_content_description),
                text = stringResource(R.string.add_institution_fab_text),
                onClick = {
                    keyboardController?.hide()
                    viewModel.onEvent(CreateParticipantEvent.Save)
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    )
}