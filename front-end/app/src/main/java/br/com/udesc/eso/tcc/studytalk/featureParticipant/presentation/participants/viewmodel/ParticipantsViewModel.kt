package br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.participants.viewmodel

import android.content.SharedPreferences
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ParticipantsViewModel @Inject constructor(
    sharedPreferences: SharedPreferences
) : StudyTalkViewModel() {

}