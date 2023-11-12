package br.com.udesc.eso.tcc.studytalk.core.presentation.activity.initial

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor() : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _route = mutableStateOf(InitialScreens.SignInScreen.route)
    val route: State<String> = _route

    init {
        checkAuthentication()
        _isLoading.value = false
    }

    private fun checkAuthentication() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            _route.value = InitialScreens.BaseActivity.route
        }
    }
}