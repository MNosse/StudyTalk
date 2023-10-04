package br.com.udesc.eso.tcc.studytalk.entity.question.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class QuestionIsNotFromParticipantException : Exception(
    "A questão não pertence ao participante."
)