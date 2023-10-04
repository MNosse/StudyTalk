package br.com.udesc.eso.tcc.studytalk.entity.answer.model

import br.com.udesc.eso.tcc.studytalk.core.interfaces.Postable
import br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant
import br.com.udesc.eso.tcc.studytalk.entity.question.model.Question
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
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