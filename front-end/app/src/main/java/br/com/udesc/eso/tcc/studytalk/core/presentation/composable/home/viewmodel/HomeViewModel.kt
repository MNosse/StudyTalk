package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.home.viewmodel

import android.content.SharedPreferences
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    sharedPreferences: SharedPreferences
) : StudyTalkViewModel() {

}