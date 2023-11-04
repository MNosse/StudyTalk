package br.com.udesc.eso.tcc.studytalk.core.presentation.activity.initial

sealed class InitialScreens(val route: String) {
    object AboutScreen : InitialScreens("about_screen")
    object BaseActivity : InitialScreens("base_activity")
    object SignInScreen : InitialScreens("sign_in_screen")
    object SignUpScreen : InitialScreens("sign_up_screen")
}
