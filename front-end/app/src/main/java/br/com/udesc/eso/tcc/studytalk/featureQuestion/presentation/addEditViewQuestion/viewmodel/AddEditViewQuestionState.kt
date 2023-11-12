package br.com.udesc.eso.tcc.studytalk.featureQuestion.presentation.addEditViewQuestion.viewmodel

import br.com.udesc.eso.tcc.studytalk.featureAnswer.domain.model.Answer

data class AddEditViewQuestionState (
    val answers: List<Answer> = emptyList(),
    val likedAnswers: List<Answer> = emptyList()
)