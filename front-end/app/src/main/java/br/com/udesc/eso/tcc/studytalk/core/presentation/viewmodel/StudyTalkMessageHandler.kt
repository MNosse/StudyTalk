package br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import br.com.udesc.eso.tcc.studytalk.core.utils.UiText

object StudyTalkMessageHandler {
    val message: MutableState<UiText> = mutableStateOf(UiText.DynamicString(""))
}