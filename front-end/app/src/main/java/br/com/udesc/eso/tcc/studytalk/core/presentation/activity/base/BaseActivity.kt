package br.com.udesc.eso.tcc.studytalk.core.presentation.activity.base

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.initial.InitialActivity
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.initial.InitialScreens
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.components.StudyTalkBottomNavigationBar
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.home.view.HomeScreen
import br.com.udesc.eso.tcc.studytalk.featureAnswer.presentation.view.AddEditAnswerScreen
import br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.addEditViewInstitution.view.AddEditViewInstitutionScreen
import br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.institutions.view.InstitutionsScreen
import br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.participants.view.ParticipantsScreen
import br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.profile.view.ProfileScreen
import br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.waitingApprove.view.WaitingApproveScreen
import br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.addEditViewQuestion.view.AddEditViewQuestionScreen
import br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.questions.view.QuestionsScreen
import br.com.udesc.eso.tcc.studytalk.ui.theme.StudyTalkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BaseActivity : ComponentActivity() {

    private val viewModel: BaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyTalkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val bottomNavigationBarVisibility = remember { mutableStateOf(false) }
                    val selectedItemIndex = remember { mutableIntStateOf(0) }

                    Scaffold(
                        content = { paddingValues ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .systemBarsPadding()
                                    .padding(paddingValues)
                            ) {
                                NavHost(
                                    navController = navController,
                                    startDestination = viewModel.route.value
                                ) {
                                    composable(
                                        route = BaseScreens.AddEditAnswerScreen.route + "?questionId={questionId}&answerId={answerId}",
                                        arguments = listOf(
                                            navArgument(
                                                name = "questionId"
                                            ) {
                                                type = NavType.LongType
                                                defaultValue = -1L
                                            },
                                            navArgument(
                                                name = "answerId"
                                            ) {
                                                type = NavType.LongType
                                                defaultValue = -1L
                                            }
                                        )
                                    ) {
                                        AddEditAnswerScreen(navController = navController)
                                        bottomNavigationBarVisibility.value = false
                                    }
                                    composable(
                                        route = BaseScreens.AddEditViewInstitutionScreen.route + "?institutionId={institutionId}",
                                        arguments = listOf(
                                            navArgument(
                                                name = "institutionId"
                                            ) {
                                                type = NavType.LongType
                                                defaultValue = -1L
                                            }
                                        )
                                    ) {
                                        AddEditViewInstitutionScreen(navController = navController)
                                        bottomNavigationBarVisibility.value = false
                                    }
                                    composable(
                                        route = BaseScreens.AddEditViewQuestionScreen.route + "?questionId={questionId}&backRoute={backRoute}",
                                        arguments = listOf(
                                            navArgument(
                                                name = "questionId"
                                            ) {
                                                type = NavType.LongType
                                                defaultValue = -1L
                                            },
                                            navArgument(
                                                name = "backRoute"
                                            ) {
                                                type = NavType.StringType
                                                defaultValue = ""
                                            }
                                        )
                                    ) {
                                        AddEditViewQuestionScreen(navController = navController)
                                        bottomNavigationBarVisibility.value = false
                                    }
                                    composable(route = BaseScreens.HomeScreen.route) {
                                        HomeScreen(navController = navController)
                                        bottomNavigationBarVisibility.value = true
                                    }
                                    composable(route = BaseScreens.InitialActivity.route) {
                                        val intent = Intent(this@BaseActivity, InitialActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    composable(route = BaseScreens.InstitutionsScreen.route) {
                                        InstitutionsScreen(navController = navController)
                                        bottomNavigationBarVisibility.value = true
                                    }
                                    composable(route = BaseScreens.ParticipantsScreen.route) {
                                        ParticipantsScreen(navController = navController)
                                        bottomNavigationBarVisibility.value = true
                                    }
                                    composable(route = BaseScreens.ProfileScreen.route) {
                                        ProfileScreen(navController = navController)
                                        bottomNavigationBarVisibility.value = true
                                    }
                                    composable(route = BaseScreens.ProfileScreenWithoutBottomNavBar.route) {
                                        ProfileScreen(navController = navController)
                                        bottomNavigationBarVisibility.value = false
                                    }
                                    composable(route = BaseScreens.QuestionsScreen.route) {
                                        QuestionsScreen(navController = navController)
                                        bottomNavigationBarVisibility.value = true
                                    }
                                    composable(route = BaseScreens.WaitingApproveScreen.route) {
                                        WaitingApproveScreen(navController = navController)
                                        bottomNavigationBarVisibility.value = false
                                    }
                                }
                            }
                        },
                        bottomBar = {
                            AnimatedVisibility(
                                visible = bottomNavigationBarVisibility.value,
                                enter = slideInVertically(
                                    animationSpec = tween(400)
                                ) + expandVertically(
                                    expandFrom = Alignment.Top
                                ) + fadeIn(
                                    initialAlpha = 0.3f
                                ),
                                exit = slideOutVertically() + shrinkVertically() + fadeOut()
                            ) {
                                StudyTalkBottomNavigationBar(
                                    bottomNavigationItens = viewModel.bottomNavigationItens,
                                    selectedItemIndex = selectedItemIndex.value,
                                    onClick = { index, item ->
                                        selectedItemIndex.intValue = index
                                        navController.navigate(item.route)
                                    }
                                )
                            }
                        }
                    )

                    ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
                        val bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                        view.updatePadding(bottom = bottom)
                        insets
                    }
                }
            }
        }
    }
}