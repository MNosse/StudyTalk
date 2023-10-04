package br.com.udesc.eso.tcc.studytalk.entity.report.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ReportNotFoundException : Exception(
    "A denúncia não foi encontrada."
)