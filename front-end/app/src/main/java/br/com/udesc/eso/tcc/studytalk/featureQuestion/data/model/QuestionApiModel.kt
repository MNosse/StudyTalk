package br.com.udesc.eso.tcc.studytalk.featureQuestion.data.model

import br.com.udesc.eso.tcc.studytalk.core.domain.model.Postable
import br.com.udesc.eso.tcc.studytalk.core.domain.model.Subject
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.model.InstitutionApiModel
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.model.ParticipantApiModel

class QuestionApiModel(
    val id: Long,
    var title: String,
    var description: String,
    val subjects: MutableList<Subject>,
    val participant: ParticipantApiModel,
    val institution: InstitutionApiModel
) : Postable {
    override fun getPostDescription(): String {
        return description
    }
}