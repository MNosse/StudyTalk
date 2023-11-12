package br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.favorited.view

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.udesc.eso.tcc.studytalk.R
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.base.BaseScreens
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkQuestion
import br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.favorited.viewmodel.FavoritedState
import br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.favorited.viewmodel.FavoritedViewModel

@Composable
fun FavoritedScreen(
    navController: NavController,
    viewModel: FavoritedViewModel = hiltViewModel()
) {
    DisposableEffect(navController) {
        viewModel.reload()
        onDispose {  }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp, end = 16.dp, start = 16.dp, top = 64.dp)
    ) {
        viewModel.state.value.questions.let { questions ->
            if (questions.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(questions) { question ->
                        StudyTalkQuestion(
                            text = question.title,
                            onClick = {
                                navController.navigate(route =BaseScreens.AddEditViewQuestionScreen.route + "?questionId=${question.id}&backRoute=${BaseScreens.HomeScreen.route}")
                            }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
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