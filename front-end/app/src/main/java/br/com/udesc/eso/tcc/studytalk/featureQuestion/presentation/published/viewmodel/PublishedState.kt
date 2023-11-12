package br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.published.viewmodel

import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.model.Question

data class PublishedState(
    val questions: List<Question> = emptyList()
)