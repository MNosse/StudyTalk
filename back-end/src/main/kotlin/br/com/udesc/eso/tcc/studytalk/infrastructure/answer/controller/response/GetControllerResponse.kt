package br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.response

data class Response(
    val id: Long,
    var description: String,
    var likeCount: Int
)