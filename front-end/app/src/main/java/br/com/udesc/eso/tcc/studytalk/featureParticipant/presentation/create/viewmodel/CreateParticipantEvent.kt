package br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.create.viewmodel

sealed class CreateParticipantEvent {
    data class EnteredName(val value: String) : CreateParticipantEvent()
    data class EnteredRegistrationCode(val value: String) : CreateParticipantEvent()
    object Save : CreateParticipantEvent()
}
