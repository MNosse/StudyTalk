package br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.waitingApprove.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkScreen
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkTopAppBar
import br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.waitingApprove.viewmodel.WaitingApproveViewModel

@Composable
fun WaitingApproveScreen(
    navController: NavController,
    viewModel: WaitingApproveViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            StudyTalkTopAppBar(title = stringResource(id = R.string.enrollment_request_top_app_bar_title))
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

            }
        }
    )
}