package br.com.udesc.eso.tcc.studytalk.core.presentation.composable.home.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import br.com.udesc.eso.tcc.studytalk.core.domain.model.Privilege
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkParticipantHandler
import br.com.udesc.eso.tcc.studytalk.core.presentation.viewmodel.StudyTalkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val studyTalkParticipantHandler: StudyTalkParticipantHandler
) : StudyTalkViewModel() {
    private val _currentPrivilege = mutableStateOf(Privilege.DEFAULT)
    val currentPrivilege: State<Privilege> = _currentPrivilege

    init {
        val currentParticipant = studyTalkParticipantHandler.currentParticipant?.copy()
        currentParticipant?.let {
            _currentPrivilege.value = currentParticipant.privilege
        }
    }
}