package br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.converter

fun convert(questions: MutableList<br.com.udesc.eso.tcc.studytalk.entity.question.model.Question>): MutableList<br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.response.Response> {
    return questions.map {
        convert(it)
    }.toMutableList()
}

fun convert(question: br.com.udesc.eso.tcc.studytalk.entity.question.model.Question): br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.response.Response {
    return br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.response.Response(
        id = question.id,
        title = question.title,
        description = question.description,
        subjects = question.subjects
    )
}