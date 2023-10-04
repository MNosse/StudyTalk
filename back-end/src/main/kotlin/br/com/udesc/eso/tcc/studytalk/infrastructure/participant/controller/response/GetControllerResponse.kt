package br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.response

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege

data class Response(
    val id: Long,
    val uid: String,
    var name: String,
    var privilege: Privilege = Privilege.DEFAULT,
    val institution: Institution? = null,
)

data class Institution(
    val id: Long,
    val registrationCode: String,
    var name: String
)