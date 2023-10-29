package br.com.udesc.eso.tcc.studytalk.featureAnswer.data.request

data class UpdateAnswerRequest(
    val description: String,
    val participantUid: String
)