package br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.administrator

import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.administrator.AdministratorSchema
import org.springframework.data.jpa.repository.JpaRepository

interface AdministratorRepository : JpaRepository<AdministratorSchema, Long> {
    fun findByUid(uid: String): AdministratorSchema?
}