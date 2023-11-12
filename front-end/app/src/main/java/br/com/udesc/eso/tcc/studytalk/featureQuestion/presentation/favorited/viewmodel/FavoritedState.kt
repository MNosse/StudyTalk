package br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.favorited.viewmodel

import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.model.Question

data class FavoritedState(
    val questions: List<Question> = emptyList()
)