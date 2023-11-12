package br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.participants.viewmodel

import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.model.Participant

data class ParticipantsState(
    val participants: List<Participant> = emptyList()
)
