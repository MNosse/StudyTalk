package br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.participants

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ParticipantsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

}