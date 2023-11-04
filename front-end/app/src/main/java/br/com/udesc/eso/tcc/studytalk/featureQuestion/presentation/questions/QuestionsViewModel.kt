package br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.questions

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

}