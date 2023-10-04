package br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.enrollmentRequest

import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.enrollmentRequest.EnrollmentRequestSchema
import org.springframework.data.jpa.repository.JpaRepository

interface EnrollmentRequestRepository : JpaRepository<EnrollmentRequestSchema, Long> {
    fun findAllByInstitutionId(id: Long): MutableList<EnrollmentRequestSchema>
    fun findByParticipantId(id: Long): EnrollmentRequestSchema?
}