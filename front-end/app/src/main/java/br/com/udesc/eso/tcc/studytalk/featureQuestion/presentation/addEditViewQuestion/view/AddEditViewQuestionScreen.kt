package br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.addEditViewQuestion.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
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
import br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.addEditViewQuestion.viewmodel.AddEditViewQuestionEvent
import br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.addEditViewQuestion.viewmodel.AddEditViewQuestionViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddEditViewQuestionScreen(
    navController: NavController,
    viewModel: AddEditViewQuestionViewModel = hiltViewModel()
) {
    val isOwner = viewModel.isOwner.value
    var deleteDialogVisibility by remember { mutableStateOf(false) }
    val editMode = viewModel.editMode.value
    val keyboardController = LocalSoftwareKeyboardController.current
    var moreMenuVisibility by remember { mutableStateOf(false) }
    val questionId = viewModel.currentQuestionId.value
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            StudyTalkTopAppBar(
                title = stringResource(R.string.question),
                hasNavBack = true,
                navBackAction = { navController.navigateUp() },
                actions = if (questionId != -1L && !editMode) {
                    val items = mutableListOf<StudyTalkTopAppBarDropdownMenuItem>().apply {
                        add(
                            StudyTalkTopAppBarDropdownMenuItem(
                                leadingIcon = if (viewModel.isFavorited.value) Icons.Filled.Star else Icons.Filled.StarBorder,
                                leadingDescription = stringResource(R.string.top_app_bar_dropdown_menu_item_fav_content_description),
                                text = stringResource(R.string.top_app_bar_dropdown_menu_item_fav),
                                onClick = {
                                    viewModel.onEvent(AddEditViewQuestionEvent.HandleFavorite)
                                }
                            )
                        )
                        if (isOwner) {
                            add(
                                StudyTalkTopAppBarDropdownMenuItem(
                                    leadingIcon = Icons.Filled.Edit,
                                    leadingDescription = stringResource(R.string.top_app_bar_dropdown_menu_item_edit_content_description),
                                    text = stringResource(R.string.top_app_bar_dropdown_menu_item_edit),
                                    onClick = {
                                        viewModel.onEvent(AddEditViewQuestionEvent.EnteredEditMode(true))
                                    }
                                )
                            )
                            add(
                                StudyTalkTopAppBarDropdownMenuItem(
                                    leadingIcon = Icons.Filled.Delete,
                                    leadingDescription = stringResource(R.string.top_app_bar_dropdown_menu_item_delete_content_description),
                                    text = stringResource(R.string.top_app_bar_dropdown_menu_item_delete),
                                    onClick = { deleteDialogVisibility = true }
                                )
                            )
                        }
                        add(
                            StudyTalkTopAppBarDropdownMenuItem(
                                leadingIcon = Icons.Filled.Report,
                                leadingDescription = stringResource(R.string.top_app_bar_dropdown_menu_item_report_content_description),
                                text = stringResource(R.string.top_app_bar_dropdown_menu_item_report),
                                onClick = { }
                            )
                        )
                    }
                    StudyTalkTopAppBarActions(
                        expanded = moreMenuVisibility,
                        onClick = { moreMenuVisibility = !moreMenuVisibility },
                        dropdownMenuItens = items.toList()
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
                        label = stringResource(R.string.title_label),
                        value = viewModel.title.value,
                        singleLine = true,
                        enabled = (editMode || questionId == -1L),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = {
                            viewModel.onEvent(
                                AddEditViewQuestionEvent.EnteredTitle(it)
                            )
                        }
                    )
                    StudyTalkTextField(
                        label = stringResource(R.string.description_label),
                        value = viewModel.description.value,
                        singleLine = true,
                        enabled = (editMode || questionId == -1L),
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
                                AddEditViewQuestionEvent.EnteredDescription(it)
                            )
                        },
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(text = stringResource(R.string.subjects_label))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(viewModel.subjects) { subject ->
                            InputChip(
                                modifier = Modifier.padding(end = 8.dp),
                                selected = (viewModel.selectedSubjects.contains(subject)),
                                enabled = (editMode || questionId == -1L),
                                onClick = {
                                    viewModel.onEvent(
                                        AddEditViewQuestionEvent.HandleSubject(subject)
                                    )
                                },
                                label = {
                                    Text(text = subject.value)
                                }
                            )
                        }
                    }
                }
                if (deleteDialogVisibility) {
                    AlertDialog(
                        title = {
                            Text(
                                text = stringResource(
                                    R.string.delete_dialog_title,
                                    stringResource(R.string.question)
                                )
                            )
                        },
                        text = {
                            Text(
                                text = stringResource(
                                    R.string.delete_dialog_content,
                                    stringResource(
                                        R.string.this_pronoum,
                                        stringResource(R.string.fem_gender),
                                        stringResource(R.string.question)
                                    )
                                )
                            )
                        },
                        onDismissRequest = {
                            deleteDialogVisibility = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.onEvent(AddEditViewQuestionEvent.Delete)
                                    deleteDialogVisibility = false
                                }
                            ) {
                                Text(stringResource(R.string.exclude))
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    deleteDialogVisibility = false
                                }
                            ) {
                                Text(stringResource(R.string.cancel))
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            if (questionId == -1L || editMode) {
                StudyTalkFloatingActionButton(
                    iconVector = Icons.Filled.Check,
                    iconDescription = if (editMode) {
                        stringResource(R.string.save)
                    } else {
                        stringResource(R.string.create)
                    },
                    text = if (editMode) {
                        stringResource(R.string.save)
                    } else {
                        stringResource(R.string.create)
                    },
                    onClick = {
                        keyboardController?.hide()
                        viewModel.onEvent(AddEditViewQuestionEvent.Save)
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    )
}