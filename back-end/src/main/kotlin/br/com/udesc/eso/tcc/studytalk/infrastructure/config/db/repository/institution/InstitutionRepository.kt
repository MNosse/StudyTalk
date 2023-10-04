package br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution

import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import org.springframework.data.jpa.repository.JpaRepository

interface InstitutionRepository : JpaRepository<InstitutionSchema, Long> {
    fun findByRegistrationCode(registrationCode: String): InstitutionSchema?
}