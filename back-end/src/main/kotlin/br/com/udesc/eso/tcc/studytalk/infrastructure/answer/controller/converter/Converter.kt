package br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.converter

fun convert(questions: MutableList<br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer>): MutableList<br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.response.Response> {
    return questions.map {
        convert(it)
    }.toMutableList()
}

fun convert(answer: br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer): br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.response.Response {
    return br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.response.Response(
        id = answer.id,
        description = answer.description,
        likeCount = answer.likeCount
    )
}