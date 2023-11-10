package br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.addEditViewInstitution.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkTopAppBarActions
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkTopAppBarDropdownMenuItem
import br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.addEditViewInstitution.viewmodel.AddEditViewInstitutionEvent
import br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.addEditViewInstitution.viewmodel.AddEditViewInstitutionViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddEditViewInstitutionScreen(
    navController: NavController,
    viewModel: AddEditViewInstitutionViewModel = hiltViewModel()
) {
    var deleteDialogVisible by remember { mutableStateOf(false) }
    val editMode = viewModel.editMode.value
    var expandedMoreMenu by remember { mutableStateOf(false) }
    val institutionId = viewModel.currentInstitutionId.value
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            StudyTalkTopAppBar(
                title = stringResource(R.string.institution_top_app_bar_title),
                hasNavBack = true,
                navBackAction = { navController.navigateUp() },
                actions = if (institutionId != -1L && !editMode) {
                    StudyTalkTopAppBarActions(
                        expanded = expandedMoreMenu,
                        onClick = { expandedMoreMenu = !expandedMoreMenu },
                        dropdownMenuItens = listOf(
                            StudyTalkTopAppBarDropdownMenuItem(
                                leadingIcon = Icons.Filled.Edit,
                                leadingDescription = stringResource(R.string.top_app_bar_dropdown_menu_item_edit_content_description),
                                text = stringResource(R.string.top_app_bar_dropdown_menu_item_edit),
                                onClick = {
                                    viewModel.onEvent(
                                        AddEditViewInstitutionEvent.ChangeEditMode(
                                            true
                                        )
                                    )
                                }
                            ),
                            StudyTalkTopAppBarDropdownMenuItem(
                                leadingIcon = Icons.Filled.Delete,
                                leadingDescription = stringResource(R.string.top_app_bar_dropdown_menu_item_delete_content_description),
                                text = stringResource(R.string.top_app_bar_dropdown_menu_item_delete),
                                onClick = { deleteDialogVisible = true }
                            )
                        )
                    )
                } else null
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
                        label = stringResource(R.string.name_label),
                        value = viewModel.name.value,
                        singleLine = true,
                        enabled = (editMode || institutionId == -1L),
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
                                AddEditViewInstitutionEvent.EnteredName(
                                    it
                                )
                            )
                        }
                    )
                    StudyTalkTextField(
                        label = stringResource(R.string.registration_code_label),
                        value = viewModel.registrationCode.value,
                        singleLine = true,
                        enabled = false,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        onValueChange = {},
                        removeSpacer = true
                    )
                }
                if (deleteDialogVisible) {
                    AlertDialog(
                        title = {
                            Text(
                                text = stringResource(
                                    R.string.delete_dialog_title,
                                    stringResource(R.string.institution)
                                )
                            )
                        },
                        text = {
                            Text(
                                text = stringResource(
                                    R.string.delete_dialog_content,
                                    stringResource(R.string.delete_institution_option)
                                )
                            )
                        },
                        onDismissRequest = {
                            deleteDialogVisible = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.onEvent(AddEditViewInstitutionEvent.Delete)
                                    deleteDialogVisible = false
                                }
                            ) {
                                Text(stringResource(R.string.delete_dialog_button))
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    deleteDialogVisible = false
                                }
                            ) {
                                Text(stringResource(R.string.dismiss_dialog_button))
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            if (institutionId == -1L || editMode) {
                StudyTalkFloatingActionButton(
                    iconVector = Icons.Filled.Check,
                    iconDescription = stringResource(R.string.institution_fab_content_description),
                    text = if (editMode) {
                        stringResource(R.string.edit_institution_fab_text)
                    } else {
                        stringResource(R.string.add_institution_fab_text)
                    },
                    onClick = {
                        keyboardController?.hide()
                        viewModel.onEvent(AddEditViewInstitutionEvent.Save)
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    )
}