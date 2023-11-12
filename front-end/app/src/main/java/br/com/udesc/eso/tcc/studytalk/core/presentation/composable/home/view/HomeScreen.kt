package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.home.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.domain.model.Privilege
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkScreen
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkTopAppBar
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.home.viewmodel.HomeViewModel
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.presentation.enrollmentRequesties.view.EnrollmentRequestiesScreen
import br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.participants.viewmodel.ParticipantsViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val currentPrivilege = viewModel.currentPrivilege.value
    val snackbarHostState = remember { SnackbarHostState() }
    val tabIndex = remember { mutableIntStateOf(0) }
    val tabs = mutableListOf<String>().apply {
        add("Publicadas")
        add("Favoritas")
        if (currentPrivilege == Privilege.PRINCIPAL || currentPrivilege == Privilege.TEACHER) {
            add("Solicitações")
        }
    }

    Scaffold(
        topBar = {
            StudyTalkTopAppBar(title = stringResource(R.string.studytalk_top_app_bar_title))
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
                TabRow(
                    selectedTabIndex = tabIndex.intValue,
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(text = { Text(title) },
                            selected = tabIndex.intValue == index,
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                            onClick = { tabIndex.intValue = index }
                        )
                    }
                }
                when (tabIndex.intValue) {
                    0 -> { }
                    1 -> {}
                    2 -> { EnrollmentRequestiesScreen() }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    )
}