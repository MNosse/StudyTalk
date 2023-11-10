package br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.response

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.core.enums.Subject

data class Response(
    val id: Long,
    val uid: String,
    var name: String,
    var privilege: Privilege = Privilege.DEFAULT,
    val institution: Institution? = null,
    val favoriteQuestions: MutableList<Question> = mutableListOf(),
    val likedAnswers: MutableList<Answer> = mutableListOf()
)

data class Answer(
    val id: Long,
    var description: String,
    var likeCount: Int
)

data class Institution(
    val id: Long,
    val registrationCode: String,
    var name: String
)

data class Question(
    val id: Long,
    var title: String,
    var description: String,
    val subjects: MutableList<Subject>
)