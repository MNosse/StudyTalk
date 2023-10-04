package br.com.udesc.eso.tcc.studytalk.infrastructure.report.controller.converter

fun convert(reports: MutableList<br.com.udesc.eso.tcc.studytalk.entity.report.model.Report>): MutableList<br.com.udesc.eso.tcc.studytalk.infrastructure.report.controller.response.Response> {
    return reports.map {
        convert(it)
    }.toMutableList()
}

fun convert(report: br.com.udesc.eso.tcc.studytalk.entity.report.model.Report): br.com.udesc.eso.tcc.studytalk.infrastructure.report.controller.response.Response {
    return br.com.udesc.eso.tcc.studytalk.infrastructure.report.controller.response.Response(
        id = report.id,
        postable = report.postable,
        description = report.description
    )
}