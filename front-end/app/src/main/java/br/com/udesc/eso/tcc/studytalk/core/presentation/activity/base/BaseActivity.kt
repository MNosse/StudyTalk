package br.com.udesc.eso.tcc.studytalk.core.presentation.activity.base

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.institutions.InstitutionsScreen
import br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.create.CreateParticipantScreen
import br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.participants.ParticipantsScreen
import br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.waitingApprove.WaitingApproveInScreen
import br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.questions.QuestionsScreen
import br.com.udesc.eso.tcc.studytalk.ui.theme.StudyTalkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BaseActivity : ComponentActivity() {

    private val viewModel: BaseViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyTalkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                viewModel.bottomNavigationItens.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = index == viewModel.selectedItemIndex.value,
                                        onClick = {
                                            viewModel.onEvent(BaseEvent.ChangeCurrentIndex(index))
                                            navController.navigate(item.route)
                                        },
                                        label = {
                                            Text(text = item.title)
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (index == viewModel.selectedItemIndex.value) {
                                                    item.selectedIcon
                                                } else item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = viewModel.route.value
                        ) {
                            composable(route = BaseScreens.CreateParticipantScreen.route) {
                                CreateParticipantScreen(navController = navController)
                            }
                            composable(route = BaseScreens.InstitutionsScreen.route) {
                                InstitutionsScreen(navController = navController)
                            }
                            composable(route = BaseScreens.ParticipantsScreen.route) {
                                ParticipantsScreen(navController = navController)
                            }
                            composable(route = BaseScreens.QuestionsScreen.route) {
                                QuestionsScreen(navController = navController)
                            }
                            composable(route = BaseScreens.WaitingApproveScreen.route) {
                                WaitingApproveInScreen(navController = navController)
                            }
                        }
                    }

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