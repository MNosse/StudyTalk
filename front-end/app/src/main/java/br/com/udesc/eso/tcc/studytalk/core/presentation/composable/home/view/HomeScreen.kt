package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.home.view

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
import br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.participants.viewmodel.ParticipantsViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: ParticipantsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            StudyTalkTopAppBar(title = stringResource(R.string.studytalk_top_app_bar_title))
        }
    ) {
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
}