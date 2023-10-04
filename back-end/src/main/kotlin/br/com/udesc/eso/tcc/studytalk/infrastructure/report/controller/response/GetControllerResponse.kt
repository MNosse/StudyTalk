package br.com.udesc.eso.tcc.studytalk.infrastructure.report.controller.response

import br.com.udesc.eso.tcc.studytalk.core.interfaces.Postable

data class Response(
    val id: Long = 0L,
    val postable: Postable,
    val description: String,
)