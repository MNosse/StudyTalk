package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.model

import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.model.Institution
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.model.Participant

data class EnrollmentRequest(
    val id: Long = 0L,
    val institution: Institution,
    val participant: Participant
)