package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController,
    viewModel: AboutViewModel = hiltViewModel()
) {
    val newMode = viewModel.newMode.value
    val showChangeModeDialog = viewModel.showChangeModeDialog.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Sobre")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.surface,
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Go Back",
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            )
        }
    ) {
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
                        onClick = { viewModel.onEvent(AboutEvent.LogoClick) },
                        modifier = Modifier
                            .size(100.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MenuBook,
                            contentDescription = "StudyTalk Logo",
                            modifier = Modifier
                                .size(80.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "O StudyTalk é uma aplicação que busca auxiliar na educação de estudates através do debate. Aqui você encontrará uma vasta biblioteca de perguntas e respostas.",
                        textAlign = TextAlign.Justify
                    )
                }
                Text(

                    text = "Desenvolvido por: Mateus Coelho Nosse",
                    textAlign = TextAlign.Center
                )

                if (showChangeModeDialog) {
                    AlertDialog(
                        title = {
                            Text(text = "Mudança de Modo")
                        },
                        text = {
                            Text(text = "Você deseja mudar o modo da aplicação para $newMode")
                        },
                        onDismissRequest = {
                            viewModel.onEvent(AboutEvent.HideDialog)
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.onEvent(AboutEvent.ChangeMode)
                                }
                            ) {
                                Text("Confirmar")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    viewModel.onEvent(AboutEvent.HideDialog)
                                }
                            ) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
            }
        }
    }
}