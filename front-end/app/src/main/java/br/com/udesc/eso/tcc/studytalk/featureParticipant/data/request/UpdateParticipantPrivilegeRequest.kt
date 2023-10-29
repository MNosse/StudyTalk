package br.com.udesc.eso.tcc.studytalk.featureParticipant.data.request

import br.com.udesc.eso.tcc.studytalk.core.domain.model.Privilege

data class UpdateParticipantPrivilegeRequest(
    val requestingUid: String,
    val isAdministrator: Boolean,
    val privilege: Privilege,
)