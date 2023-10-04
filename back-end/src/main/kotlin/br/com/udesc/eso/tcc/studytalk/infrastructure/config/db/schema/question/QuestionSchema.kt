package br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.core.interfaces.Postable
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import jakarta.persistence.*

@Entity
@Table(name = "question")
data class QuestionSchema(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(length = 256, nullable = false)
    var title: String,
    @Column(length = 512, nullable = false)
    var description: String,
    @ElementCollection(targetClass = Subject::class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    val subjects: MutableList<Subject>,
    @ManyToOne
    @JoinColumn(name = "participant_id", nullable = false, updatable = false)
    val participant: ParticipantSchema,
    @ManyToOne
    @JoinColumn(name = "institution_id", nullable = false, updatable = false)
    val institution: InstitutionSchema
) : Postable {
    override fun getPostDescription(): String {
        return description
    }
}