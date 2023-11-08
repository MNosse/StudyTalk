package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.about.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.about.viewmodel.AboutEvent
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.about.viewmodel.AboutViewModel
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkScreen
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkTopAppBar

@Composable
fun AboutScreen(
    navController: NavController,
    viewModel: AboutViewModel = hiltViewModel()
) {
    val newMode = viewModel.newMode.value
    var changeModeDialogVisible by remember { mutableStateOf(false) }
    var logoClickCount by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage = viewModel.snackbarMessage.value.asString()

    Scaffold(
        topBar = {
            StudyTalkTopAppBar(
                title = stringResource(R.string.about_top_app_bar_title),
                hasNavBack = true,
                navBackAction = { navController.navigateUp() }
            )
        },
        content = {
            if (snackbarMessage.isNotBlank()) {
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(
                        message = String(snackbarMessage.toByteArray()),
                        duration = SnackbarDuration.Short
                    )
                    viewModel.onEvent(AboutEvent.ClearSnackbarMessage)
                }
            }

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
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {
                                logoClickCount++
                                if (logoClickCount >= 5) {
                                    logoClickCount = 0
                                    changeModeDialogVisible = true
                                }
                            },
                            modifier = Modifier
                                .size(100.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MenuBook,
                                contentDescription = stringResource(R.string.studytalk_logo_content_description),
                                modifier = Modifier
                                    .size(80.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.about_description_text),
                            textAlign = TextAlign.Justify
                        )
                    }
                    Text(

                        text = stringResource(R.string.about_developed_by_text),
                        textAlign = TextAlign.Center
                    )

                    if (changeModeDialogVisible) {
                        AlertDialog(
                            title = {
                                Text(text = stringResource(R.string.change_mode_dialog_title))
                            },
                            text = {
                                Text(
                                    text = stringResource(
                                        R.string.change_mode_dialog_content,
                                        newMode
                                    )
                                )
                            },
                            onDismissRequest = {
                                changeModeDialogVisible = false
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        viewModel.onEvent(AboutEvent.ChangeMode)
                                        changeModeDialogVisible = false
                                    }
                                ) {
                                    Text(stringResource(R.string.confirm_dialog_button))
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        changeModeDialogVisible = false
                                    }
                                ) {
                                    Text(stringResource(R.string.dismiss_dialog_button))
                                }
                            }
                        )
                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    )
}