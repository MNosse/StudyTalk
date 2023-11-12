package br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.response

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.core.enums.Subject

data class Response(
    val id: Long,
    val description: String,
    val likeCount: Int,
    val question: Question? = null,
    val participant: Participant? = null
)

data class Question(
    val id: Long,
    val title: String,
    val description: String,
    val subjects: MutableList<Subject>
)

data class Participant(
    val id: Long,
    val uid: String,
    val name: String,
    val privilege: Privilege,
)