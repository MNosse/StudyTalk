package br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.questions.view

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkQuestion
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkScreen
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkTopAppBar
import br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.questions.viewmodel.QuestionsViewModel

@Composable
fun QuestionsScreen(
    navController: NavController,
    viewModel: QuestionsViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            StudyTalkTopAppBar(title = stringResource(id = R.string.questions_top_app_bar_title))
        },
        content = {
            val context = LocalContext.current
            val state = viewModel.state.value

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
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(state.questions) { question ->
                            StudyTalkQuestion(
                                text = question.title,
                                onClick = {
                                    navController.navigate(BaseScreens.AddEditViewQuestionScreen.route + "?questionId=${question.id}&backRoute=${BaseScreens.QuestionsScreen.route}")
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
                iconDescription = stringResource(R.string.questions_fab_content_description),
                onClick = { navController.navigate(BaseScreens.AddEditViewQuestionScreen.route) }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    )
}