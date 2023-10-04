package br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.report

import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.report.ReportSchema
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ReportRepository : JpaRepository<ReportSchema, Long> {
    @Query(
        value = "SELECT report.* " +
                "FROM report AS report " +
                "LEFT JOIN answer AS answer ON report.postable_type = 'ANSWER' AND report.postable_id = answer.id " +
                "LEFT JOIN question AS question ON report.postable_type = 'QUESTION' AND report.postable_id = question.id " +
                "INNER JOIN participant AS participant ON participant.id = COALESCE(answer.participant_id, question.participant_id) " +
                "WHERE participant.institution_id = :id",
        nativeQuery = true)
    fun findAllByInstitutionId(id: Long): MutableList<ReportSchema>
}