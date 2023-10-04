package br.com.udesc.eso.tcc.studytalk.entity.question.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class QuestionNotFoundException : Exception(
    "A questão não foi encontrada."
)