package br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant

import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import org.springframework.data.jpa.repository.JpaRepository

interface ParticipantRepository : JpaRepository<ParticipantSchema, Long> {
    fun findAllByInstitutionId(id: Long): MutableList<ParticipantSchema>
    fun findByUid(uid: String): ParticipantSchema?
}