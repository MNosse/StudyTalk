package br.com.udesc.eso.tcc.studytalk.featureQuestion.data.response

import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.model.QuestionApiModel

data class GetAllQuestionsResponse(
    val questions: MutableList<QuestionApiModel>
)