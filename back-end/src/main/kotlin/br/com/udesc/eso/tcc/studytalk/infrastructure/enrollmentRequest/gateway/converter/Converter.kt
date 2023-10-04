package br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.gateway.converter

import br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.model.EnrollmentRequest
import br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution
import br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.enrollmentRequest.EnrollmentRequestSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema

fun convert(enrollmentRequestSchema: EnrollmentRequestSchema): EnrollmentRequest {
    return EnrollmentRequest(
        id = enrollmentRequestSchema.id,
        institution = convert(enrollmentRequestSchema.institution),
        participant = convert(enrollmentRequestSchema.participant)
    )
}

private fun convert(institutionSchema: InstitutionSchema): Institution {
    return Institution(
        id = institutionSchema.id,
        registrationCode = institutionSchema.registrationCode,
        name = institutionSchema.name
    )
}

private fun convert(participantSchema: ParticipantSchema): Participant {
    return Participant(
        id = participantSchema.id,
        uid = participantSchema.uid,
        name = participantSchema.name,
        privilege = participantSchema.privilege
    )
}