package br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.questions.viewmodel

import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.model.Question

data class QuestionsState(
    val questions: List<Question> = emptyList()
)