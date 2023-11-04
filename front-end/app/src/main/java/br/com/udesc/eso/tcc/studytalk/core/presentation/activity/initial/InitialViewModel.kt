package br.com.udesc.eso.tcc.studytalk.core.presentation.activity.initial

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _route = mutableStateOf("")
    val route: State<String> = _route

    init {
        checkAuthentication()
        _isLoading.value = false
    }

    private fun checkAuthentication() {
        _route.value = InitialScreens.SignInScreen.route
        val auth = FirebaseAuth.getInstance()
        try {
            if (auth.currentUser != null) {
                sharedPreferences.edit().putString("current_uid", auth.uid).apply()
                _route.value = InitialScreens.BaseActivity.route
            }
        } catch (e: Exception) {
            _route.value = InitialScreens.SignInScreen.route
            sharedPreferences.edit().putString("current_uid", "").apply()
            auth.signOut()
        }
    }
}