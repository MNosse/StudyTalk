package br.com.udesc.eso.tcc.studytalk.entity.report.gateway

import br.com.udesc.eso.tcc.studytalk.entity.report.model.Report

interface ReportGateway {
    fun approve(id: Long)
    fun create(postableId: Long, postableType: String, description: String)
    fun getAllByInstitutionId(id: Long): MutableList<Report>
    fun getById(id: Long): Report?
    fun reprove(id: Long)
}