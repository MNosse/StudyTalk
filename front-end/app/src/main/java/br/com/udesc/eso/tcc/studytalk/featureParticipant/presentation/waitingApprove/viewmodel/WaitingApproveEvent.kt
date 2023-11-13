package br.com.udesc.eso.tcc.studytalk.featureParticipant.presentation.waitingApprove.viewmodel

sealed class WaitingApproveEvent {
    object Reload : WaitingApproveEvent()
    object SignOut : WaitingApproveEvent()
}
