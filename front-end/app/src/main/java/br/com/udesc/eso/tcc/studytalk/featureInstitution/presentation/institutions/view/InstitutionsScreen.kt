package br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.institutions.view

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.base.BaseScreens
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkCardItem
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkFloatingActionButton
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkScreen
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkTopAppBar
import br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.institutions.viewmodel.InstitutionsViewModel

@Composable
fun InstitutionsScreen(
    navController: NavController,
    viewModel: InstitutionsViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            StudyTalkTopAppBar(title = stringResource(R.string.institutions_top_app_bar_title))
        },
        content = {
            val context = LocalContext.current

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
                        .padding(16.dp)
                ) {
                    viewModel.state.value.institutions.let { institutions ->
                        if (institutions.isNotEmpty()) {
                            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                items(institutions) { institution ->
                                    StudyTalkCardItem(
                                        text = institution.name,
                                        onClick = {
                                            navController.navigate(BaseScreens.AddEditViewInstitutionScreen.route + "?institutionId=${institution.id}")
                                        }
                                    )
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(48.dp),
                                    imageVector = Icons.Filled.SearchOff,
                                    contentDescription = stringResource(R.string.no_results),
                                    tint = MaterialTheme.colorScheme.outlineVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.no_results),
                                    fontSize = 24.sp,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }
                    }
                }
            }

            BackHandler {
                (context as ComponentActivity).finish()
            }
        },
        floatingActionButton = {
            StudyTalkFloatingActionButton(
                iconVector = Icons.Filled.Add,
                iconDescription = stringResource(R.string.institutions_fab_content_description),
                onClick = { navController.navigate(BaseScreens.AddEditViewInstitutionScreen.route) }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    )
}