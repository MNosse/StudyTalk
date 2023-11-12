package br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.profile.viewmodel

sealed class ProfileEvent {
    data class EnteredName(val value: String) : ProfileEvent()
    data class EnteredRegistrationCode(val value: String) : ProfileEvent()
    object Save : ProfileEvent()
    object SignOut : ProfileEvent()
}
