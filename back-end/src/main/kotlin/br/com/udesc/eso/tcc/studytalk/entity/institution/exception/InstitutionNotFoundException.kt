package br.com.udesc.eso.tcc.studytalk.entity.institution.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class InstitutionNotFoundException : Exception(
    "A instituição não foi encontrada."
)