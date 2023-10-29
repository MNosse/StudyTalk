package br.com.udesc.eso.tcc.studytalk.featureAnswer.data.model

import br.com.udesc.eso.tcc.studytalk.core.domain.model.Postable
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.model.ParticipantApiModel
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.model.QuestionApiModel

class AnswerApiModel(
    val id: Long,
    var description: String,
    var likeCount: Int,
    val question: QuestionApiModel,
    val participant: ParticipantApiModel
) : Postable {
    override fun getPostDescription(): String {
        return description
    }
}