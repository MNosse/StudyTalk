package br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer

import br.com.udesc.eso.tcc.studytalk.core.interfaces.Postable
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import jakarta.persistence.*

@Entity
@Table(name = "answer")
data class AnswerSchema(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(length = 512, nullable = false)
    var description: String,
    @Column(nullable = false)
    var likeCount: Int = 0,
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false, updatable = false)
    val question: QuestionSchema,
    @ManyToOne
    @JoinColumn(name = "participant_id", nullable = false, updatable = false)
    val participant: ParticipantSchema
) : Postable {
    override fun getPostDescription(): String {
        return description
    }
}