package br.com.udesc.eso.tcc.studytalk.entity.participant.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class ParticipantWithoutPermissionException : Exception(
    "O participante não possui a permissão necessária."
)