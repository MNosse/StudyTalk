package br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.model

import br.com.udesc.eso.tcc.studytalk.core.domain.model.Privilege
import br.com.udesc.eso.tcc.studytalk.featureAnswer.domain.model.Answer
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.model.Institution
import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.model.Question

data class Participant(
    val id: Long = 0L,
    val uid: String,
    var name: String,
    var privilege: Privilege = Privilege.DEFAULT,
    val institution: Institution? = null,
    val favoriteQuestions: MutableList<Question> = mutableListOf(),
    val likedAnswers: MutableList<Answer> = mutableListOf()
)