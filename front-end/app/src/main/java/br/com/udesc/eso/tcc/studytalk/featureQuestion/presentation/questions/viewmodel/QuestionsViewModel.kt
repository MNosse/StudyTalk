package br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.questions.viewmodel

import android.content.SharedPreferences
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : StudyTalkViewModel() {

}