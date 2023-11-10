package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.converter

import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.entity.EnrollmentRequestRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.model.EnrollmentRequestApiModel
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.model.EnrollmentRequest
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.entity.InstitutionRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.entity.ParticipantRoomEntity

fun convertToModel(
    enrollmentRequest: EnrollmentRequestRoomEntity,
    institution: InstitutionRoomEntity,
    participant: ParticipantRoomEntity
): EnrollmentRequest {
    return EnrollmentRequest(
        id = enrollmentRequest.id,
        institution = br.com.udesc.eso.tcc.studytalk.featureInstitution.data.converter.convertToModel(
            institution
        ),
        participant = br.com.udesc.eso.tcc.studytalk.featureParticipant.data.converter.convertToModel(
            participant
        )
    )
}

fun convertToModel(enrollmentRequest: EnrollmentRequestApiModel): EnrollmentRequest {
    return EnrollmentRequest(
        id = enrollmentRequest.id,
        institution = br.com.udesc.eso.tcc.studytalk.featureInstitution.data.converter.convertToModel(
            enrollmentRequest.institution
        )!!,
        participant = br.com.udesc.eso.tcc.studytalk.featureParticipant.data.converter.convertToModel(
            enrollmentRequest.participant
        )!!
    )
}

fun convertToRoomEntity(enrollmentRequest: EnrollmentRequestApiModel): EnrollmentRequestRoomEntity {
    return EnrollmentRequestRoomEntity(
        id = enrollmentRequest.id,
        institutionId = enrollmentRequest.institution.id,
        participantId = enrollmentRequest.participant.id
    )
}