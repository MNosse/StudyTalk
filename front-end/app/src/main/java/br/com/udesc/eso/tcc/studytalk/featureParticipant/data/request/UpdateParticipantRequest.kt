package br.com.udesc.eso.tcc.studytalk.featureParticipant.data.request

data class UpdateParticipantRequest(
    val requestingParticipantUid: String,
    val name: String
)