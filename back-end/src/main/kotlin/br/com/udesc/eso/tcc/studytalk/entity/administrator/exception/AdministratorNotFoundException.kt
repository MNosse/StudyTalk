package br.com.udesc.eso.tcc.studytalk.entity.administrator.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class AdministratorNotFoundException : Exception(
    "O administrador não foi encontrado."
)