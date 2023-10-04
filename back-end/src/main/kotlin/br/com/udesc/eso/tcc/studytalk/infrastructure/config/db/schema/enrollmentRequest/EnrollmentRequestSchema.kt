package br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.enrollmentRequest

import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import jakarta.persistence.*

@Entity
@Table(name = "enrollment_request")
data class EnrollmentRequestSchema(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "institution_id", nullable = false, updatable = false)
    val institution: InstitutionSchema,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participant_id", nullable = false, updatable = false)
    val participant: ParticipantSchema
)