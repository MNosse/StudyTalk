package br.com.udesc.eso.tcc.studytalk.featureAnswer.domain.model

import br.com.udesc.eso.tcc.studytalk.core.domain.model.Postable
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.model.Participant
import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.model.Question

data class Answer(
    val id: Long = 0L,
    var description: String,
    var likeCount: Int = 0,
    val question: Question? = null,
    val participant: Participant? = null
) : Postable {
    override fun getPostDescription(): String {
        return description
    }
}