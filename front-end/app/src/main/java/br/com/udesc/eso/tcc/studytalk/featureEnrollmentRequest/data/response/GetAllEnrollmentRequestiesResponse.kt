package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.response

import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.model.EnrollmentRequestApiModel

data class GetAllEnrollmentRequestiesResponse(
    val enrollmentRequesties: MutableList<EnrollmentRequestApiModel>
)