package br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.gateway

import br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.model.EnrollmentRequest

interface EnrollmentRequestGateway {
    fun approve(id: Long)
    fun getAllByInstitutionId(id: Long): MutableList<EnrollmentRequest>
    fun getById(id: Long): EnrollmentRequest?
    fun getByParticipantId(id: Long): EnrollmentRequest?
    fun reprove(id: Long)
}