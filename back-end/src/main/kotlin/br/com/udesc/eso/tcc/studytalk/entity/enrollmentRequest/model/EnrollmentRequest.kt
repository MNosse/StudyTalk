package br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.model

import br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution
import br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant

data class EnrollmentRequest(
    val id: Long = 0L,
    val institution: Institution,
    val participant: Participant
)