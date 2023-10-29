package br.com.udesc.eso.tcc.studytalk.featureParticipant.data.request

data class CreateParticipantRequest(
    val registrationCode: String,
    val uid: String,
    val name: String
)