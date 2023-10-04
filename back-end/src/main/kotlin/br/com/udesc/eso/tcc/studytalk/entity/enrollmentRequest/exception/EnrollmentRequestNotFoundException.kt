package br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class EnrollmentRequestNotFoundException : Exception(
    "A solicitação de cadastro não foi encontrada."
)