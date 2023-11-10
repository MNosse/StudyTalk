package br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.response

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.core.enums.Subject

data class Response(
    val id: Long,
    var title: String,
    var description: String,
    val subjects: MutableList<Subject>,
    val participant: Participant? = null,
    val institution: Institution? = null
)

data class Participant(
    val id: Long,
    val uid: String,
    var name: String,
    var privilege: Privilege
)

data class Institution(
    val id: Long,
    val registrationCode: String,
    var name: String
)