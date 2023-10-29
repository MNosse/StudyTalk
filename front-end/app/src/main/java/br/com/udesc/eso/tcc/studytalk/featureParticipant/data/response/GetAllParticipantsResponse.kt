package br.com.udesc.eso.tcc.studytalk.featureParticipant.data.response

import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.model.ParticipantApiModel

data class GetAllParticipantsResponse(
    val participants: MutableList<ParticipantApiModel>
)