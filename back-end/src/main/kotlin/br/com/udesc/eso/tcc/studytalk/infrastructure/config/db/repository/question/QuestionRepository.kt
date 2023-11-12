package br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question

import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionRepository : JpaRepository<QuestionSchema, Long> {
    fun findAllByInstitutionIdOrderById(id: Long): MutableList<QuestionSchema>
}