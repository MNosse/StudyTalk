package br.com.udesc.eso.tcc.studytalk.featureAnswer.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import br.com.udesc.eso.tcc.studytalk.featureAnswer.presentation.viewmodel.AddEditAnswerEvent
import br.com.udesc.eso.tcc.studytalk.featureAnswer.presentation.viewmodel.AddEditAnswerViewModel
import br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.addEditViewInstitution.viewmodel.AddEditViewInstitutionEvent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddEditAnswerScreen(
    navController: NavController,
    viewModel: AddEditAnswerViewModel = hiltViewModel()
) {
    val editMode = viewModel.editMode.value
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            StudyTalkTopAppBar(
                title = stringResource(R.string.answer),
                hasNavBack = true,
                navBackAction = { navController.navigateUp() }
            )
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
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    StudyTalkTextField(
                        label = stringResource(R.string.description_label),
                        value = viewModel.description.value,
                        singleLine = false,
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
                                AddEditAnswerEvent.EnteredDescription(
                                    it
                                )
                            )
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            StudyTalkFloatingActionButton(
                iconVector = Icons.Filled.Check,
                iconDescription = if (editMode) {
                    stringResource(R.string.edit_institution_fab_text)
                } else {
                    stringResource(R.string.add_institution_fab_text)
                },
                text = if (editMode) {
                    stringResource(R.string.edit_institution_fab_text)
                } else {
                    stringResource(R.string.add_institution_fab_text)
                },
                onClick = {
                    keyboardController?.hide()
                    viewModel.onEvent(AddEditAnswerEvent.Save)
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    )
}