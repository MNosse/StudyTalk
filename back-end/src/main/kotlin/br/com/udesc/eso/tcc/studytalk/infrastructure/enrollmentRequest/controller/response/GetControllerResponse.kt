package br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.response

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege

data class Response(
    val id: Long,
    val institution: Institution,
    val participant: Participant
)

data class Institution(
    val id: Long,
    val registrationCode: String,
    val name: String
)

data class Participant(
    val id: Long,
    val uid: String,
    val name: String,
    val privilege: Privilege
)