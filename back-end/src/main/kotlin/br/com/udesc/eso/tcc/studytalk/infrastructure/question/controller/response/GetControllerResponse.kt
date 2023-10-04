package br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.response

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject

data class Response(
    val id: Long,
    var title: String,
    var description: String,
    val subjects: MutableList<Subject>
)