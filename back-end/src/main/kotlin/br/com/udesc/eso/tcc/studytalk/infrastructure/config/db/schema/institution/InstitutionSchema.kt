package br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution

import br.com.udesc.eso.tcc.studytalk.core.utils.generateCode
import jakarta.persistence.*

@Entity
@Table(name = "institution")
data class InstitutionSchema(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(name = "registration_code", length = 8, updatable = false, nullable = false)
    val registrationCode: String = generateCode(),
    @Column(length = 256, nullable = false)
    var name: String
)