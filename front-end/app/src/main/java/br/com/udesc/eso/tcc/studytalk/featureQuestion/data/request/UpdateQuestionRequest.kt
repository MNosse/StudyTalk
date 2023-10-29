package br.com.udesc.eso.tcc.studytalk.featureQuestion.data.request

import br.com.udesc.eso.tcc.studytalk.core.domain.model.Subject

data class UpdateQuestionRequest(
    val title: String?,
    val description: String?,
    val subjects: MutableList<Subject>?,
    val participantUid: String
)