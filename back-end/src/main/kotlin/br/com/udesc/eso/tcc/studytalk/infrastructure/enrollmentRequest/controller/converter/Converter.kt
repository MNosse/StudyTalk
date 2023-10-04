package br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.converter

fun convert(enrollmentRequesties: MutableList<br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.model.EnrollmentRequest>): MutableList<br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.response.Response> {
    return enrollmentRequesties.map {
        convert(it)
    }.toMutableList()
}

fun convert(enrollmentRequest: br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.model.EnrollmentRequest): br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.response.Response {
    return br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.response.Response(
        id = enrollmentRequest.id,
        institution = convert(enrollmentRequest.institution),
        participant = convert(enrollmentRequest.participant)
    )
}

private fun convert(institution: br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution): br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.response.Institution {
    return br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.response.Institution(
        id = institution.id,
        registrationCode = institution.registrationCode!!,
        name = institution.name
    )
}

private fun convert(participant: br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant): br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.response.Participant {
    return br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.response.Participant(
        id = participant.id,
        uid = participant.uid,
        name = participant.name,
        privilege = participant.privilege
    )
}