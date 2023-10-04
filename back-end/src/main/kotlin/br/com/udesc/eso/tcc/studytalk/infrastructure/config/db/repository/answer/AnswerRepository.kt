package br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.answer

import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer.AnswerSchema
import org.springframework.data.jpa.repository.JpaRepository

interface AnswerRepository : JpaRepository<AnswerSchema, Long> {
    fun findAllByQuestionId(id: Long): MutableList<AnswerSchema>
}