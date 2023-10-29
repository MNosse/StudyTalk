package br.com.udesc.eso.tcc.studytalk.featureAnswer.data.response

import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.model.AnswerApiModel

data class GetAllAnswersResponse(
    val answers: MutableList<AnswerApiModel>
)
