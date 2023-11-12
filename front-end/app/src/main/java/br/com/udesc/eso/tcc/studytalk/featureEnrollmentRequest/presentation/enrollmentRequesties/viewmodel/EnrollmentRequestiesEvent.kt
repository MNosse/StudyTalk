package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.presentation.enrollmentRequesties.viewmodel

sealed class EnrollmentRequestiesEvent {
    data class Approve(val value: Long) : EnrollmentRequestiesEvent()
    data class Reprove(val value: Long) : EnrollmentRequestiesEvent()
}