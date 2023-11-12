package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.presentation.enrollmentRequesties.viewmodel

import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.model.EnrollmentRequest

data class EnrollmentRequestiesState(
    val enrollmentRequesties: List<EnrollmentRequest> = emptyList()
)
