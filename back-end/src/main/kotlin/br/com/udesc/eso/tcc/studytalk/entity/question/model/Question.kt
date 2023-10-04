package br.com.udesc.eso.tcc.studytalk.entity.question.model

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.core.interfaces.Postable
import br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution
import br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Question(
    val id: Long = 0L,
    var title: String,
    var description: String,
    val subjects: MutableList<Subject>,
    val participant: Participant? = null,
    val institution: Institution? = null
) : Postable {
    override fun getPostDescription(): String {
        return description
    }
}