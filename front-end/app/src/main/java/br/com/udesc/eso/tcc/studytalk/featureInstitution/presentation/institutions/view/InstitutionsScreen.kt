package br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.institutions.view

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.base.BaseScreens
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkFloatingActionButton
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkListItem
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkScreen
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkTopAppBar
import br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.institutions.viewmodel.InstitutionsViewModel

@Composable
fun InstitutionsScreen(
    navController: NavController,
    viewModel: InstitutionsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Scaffold(
        topBar = {
            StudyTalkTopAppBar(title = stringResource(R.string.institutions_top_app_bar_title))
        },
        content = {
            val context = LocalContext.current

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
                        .padding(16.dp)
                ) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        var index = 0
                        val size = state.institutions.size
                        items(state.institutions) { institution ->
                            index++
                            StudyTalkListItem(
                                title = institution.name,
                                showDivider = index != size,
                                onClick = {
                                    navController.navigate(BaseScreens.AddEditInstitutionScreen.route + "?institutionId=${institution.id}")
                                }
                            )
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
                onClick = { navController.navigate(BaseScreens.AddEditInstitutionScreen.route) }
            )
        },
    )
}