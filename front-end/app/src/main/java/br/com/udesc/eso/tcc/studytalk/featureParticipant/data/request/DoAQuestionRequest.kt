package br.com.udesc.eso.tcc.studytalk.featureParticipant.data.request

import br.com.udesc.eso.tcc.studytalk.core.domain.model.Subject

data class DoAQuestionRequest(
    val title: String,
    val description: String,
    val subjects: MutableList<Subject>
)