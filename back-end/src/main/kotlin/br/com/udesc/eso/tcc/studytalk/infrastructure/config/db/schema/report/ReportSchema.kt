package br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.report

import br.com.udesc.eso.tcc.studytalk.core.interfaces.Postable
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer.AnswerSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import jakarta.persistence.*
import org.hibernate.annotations.Any
import org.hibernate.annotations.AnyDiscriminator
import org.hibernate.annotations.AnyDiscriminatorValue
import org.hibernate.annotations.AnyKeyJavaClass

@Entity
@Table(name = "report")
data class ReportSchema(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Any
    @AnyDiscriminator(DiscriminatorType.STRING)
    @AnyDiscriminatorValue(discriminator = "ANSWER", entity = AnswerSchema::class)
    @AnyDiscriminatorValue(discriminator = "QUESTION", entity = QuestionSchema::class)
    @AnyKeyJavaClass(value = Long::class)
    @Column(name = "postable_type")
    @JoinColumn(name = "postable_id")
    val postable: Postable,
    @Column(length = 512, nullable = false)
    val description: String,
    @ManyToOne
    @JoinColumn(name = "institution_id", nullable = false, updatable = false)
    val institution: InstitutionSchema
)