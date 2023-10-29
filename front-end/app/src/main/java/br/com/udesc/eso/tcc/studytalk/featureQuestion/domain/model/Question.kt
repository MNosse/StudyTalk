package br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.model

import br.com.udesc.eso.tcc.studytalk.core.domain.model.Postable
import br.com.udesc.eso.tcc.studytalk.core.domain.model.Subject
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.model.Institution
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.model.Participant

data class Question(
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