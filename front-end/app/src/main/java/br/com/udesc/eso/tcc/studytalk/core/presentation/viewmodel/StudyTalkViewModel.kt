package br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

open class StudyTalkViewModel : ViewModel() {
    private val _route = mutableStateOf("")
    val route: State<String> = _route

    fun onEvent(event: StudyTalkEvent) {
        when (event) {
            is StudyTalkEvent.ClearRoute -> _route.value = ""
            is StudyTalkEvent.EnteredRoute -> _route.value = event.value
        }
    }
}