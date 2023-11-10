package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkEvent
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel

@Composable
fun StudyTalkScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: StudyTalkViewModel
) {
    val message = viewModel.message.value.asString()
    val route = viewModel.route.value

    if (route.isNotBlank()) {
        navController.navigate(route)
        viewModel.onEvent(StudyTalkEvent.ClearRoute)
    }

    if (message.isNotBlank()) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = String(message.toByteArray()),
                duration = SnackbarDuration.Short
            )
            viewModel.onEvent(StudyTalkEvent.ClearMessage)
        }
    }

}