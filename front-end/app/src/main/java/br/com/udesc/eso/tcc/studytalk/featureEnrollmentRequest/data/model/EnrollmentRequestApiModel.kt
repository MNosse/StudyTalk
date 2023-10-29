package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.model

import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.model.InstitutionApiModel
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.model.ParticipantApiModel

class EnrollmentRequestApiModel(
    val id: Long,
    val institution: InstitutionApiModel,
    val participant: ParticipantApiModel
)