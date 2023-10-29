package br.com.udesc.eso.tcc.studytalk.featureReport.data.request

data class CreateReportRequest(
    val requestingParticipantUid: String,
    val postableId: Long,
    val postableType: String,
    val description: String,
)