package br.com.udesc.eso.tcc.studytalk.featureReport.data.response

import br.com.udesc.eso.tcc.studytalk.featureReport.data.model.ReportApiModel

data class GetAllReportsResponse(
    val reports: MutableList<ReportApiModel>
)