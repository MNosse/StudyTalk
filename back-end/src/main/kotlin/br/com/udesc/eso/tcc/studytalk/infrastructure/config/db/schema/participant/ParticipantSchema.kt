package br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer.AnswerSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import jakarta.persistence.*

@Entity
@Table(name = "participant")
data class ParticipantSchema(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(nullable = false, unique = true, updatable = false)
    val uid: String,
    @Column(nullable = false, length = 128)
    var name: String,
    @Column(nullable = false)
    var privilege: Privilege = Privilege.DEFAULT,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "institution_id")
    var institution: InstitutionSchema? = null,
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "participant_favorite_questions",
        joinColumns = [JoinColumn(name = "participant_id")],
        inverseJoinColumns = [JoinColumn(name = "question_id")]
    )
    val favoriteQuestions: MutableList<QuestionSchema> = mutableListOf(),
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "participant_liked_answers",
        joinColumns = [JoinColumn(name = "participant_id")],
        inverseJoinColumns = [JoinColumn(name = "answer_id")]
    )
    val likedAnswers: MutableList<AnswerSchema> = mutableListOf()
)