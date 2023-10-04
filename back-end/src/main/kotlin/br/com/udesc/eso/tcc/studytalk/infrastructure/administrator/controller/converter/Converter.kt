package br.com.udesc.eso.tcc.studytalk.infrastructure.administrator.controller.converter

import br.com.udesc.eso.tcc.studytalk.infrastructure.administrator.controller.response.Response

fun convert(administrator: br.com.udesc.eso.tcc.studytalk.entity.administrator.model.Administrator): Response {
    return Response(
        id = administrator.id,
        uid = administrator.uid
    )
}