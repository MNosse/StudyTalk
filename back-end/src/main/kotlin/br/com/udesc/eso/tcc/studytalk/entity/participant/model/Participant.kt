package br.com.udesc.eso.tcc.studytalk.entity.participant.model

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer
import br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution
import br.com.udesc.eso.tcc.studytalk.entity.question.model.Question

data class Participant(
    val id: Long = 0L,
    val uid: String,
    var name: String,
    var privilege: Privilege = Privilege.DEFAULT,
    val institution: Institution? = null,
    val favoriteQuestions: MutableList<Question> = mutableListOf(),
    val likedAnswers: MutableList<Answer> = mutableListOf()
)