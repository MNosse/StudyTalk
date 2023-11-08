package br.com.udesc.eso.tcc.studytalk.core.presentation.activity.initial

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.base.BaseActivity
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.about.view.AboutScreen
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.signIn.view.SignInScreen
import br.com.udesc.eso.tcc.studytalk.core.presentation.composable.signUp.view.SignUpScreen
import br.com.udesc.eso.tcc.studytalk.ui.theme.StudyTalkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InitialActivity : ComponentActivity() {

    private val viewModel: InitialViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        setContent {
            StudyTalkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = viewModel.route.value
                    ) {
                        composable(route = InitialScreens.AboutScreen.route) {
                            AboutScreen(navController = navController)
                        }
                        composable(route = InitialScreens.BaseActivity.route) {
                            val intent = Intent(this@InitialActivity, BaseActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        composable(route = InitialScreens.SignInScreen.route) {
                            SignInScreen(navController = navController)
                        }
                        composable(route = InitialScreens.SignUpScreen.route) {
                            SignUpScreen(navController = navController)
                        }
                    }
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