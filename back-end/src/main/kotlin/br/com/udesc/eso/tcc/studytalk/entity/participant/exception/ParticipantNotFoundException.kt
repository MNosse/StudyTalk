package br.com.udesc.eso.tcc.studytalk.entity.participant.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ParticipantNotFoundException : Exception(
    "O participante não foi encontrado."
)