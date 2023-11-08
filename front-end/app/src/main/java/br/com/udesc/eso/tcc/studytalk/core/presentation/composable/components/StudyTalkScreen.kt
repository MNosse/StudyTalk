package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkEvent
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel

@Composable
fun StudyTalkScreen(
    navController: NavController,
    viewModel: StudyTalkViewModel
) {
    val route = viewModel.route.value

    if (route.isNotBlank()) {
        navController.navigate(route)
        viewModel.onEvent(StudyTalkEvent.ClearRoute)
    }

}