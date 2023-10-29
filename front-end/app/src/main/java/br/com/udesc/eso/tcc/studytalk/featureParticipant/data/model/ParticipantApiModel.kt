package br.com.udesc.eso.tcc.studytalk.featureParticipant.data.model

import br.com.udesc.eso.tcc.studytalk.core.domain.model.Privilege
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.model.AnswerApiModel
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.model.InstitutionApiModel
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.model.QuestionApiModel

class ParticipantApiModel(
    val id: Long,
    val uid: String,
    var name: String,
    var privilege: Privilege,
    val institution: InstitutionApiModel? = null,
    val favoriteQuestions: MutableList<QuestionApiModel> = mutableListOf(),
    val likedAnswers: MutableList<AnswerApiModel> = mutableListOf()
)