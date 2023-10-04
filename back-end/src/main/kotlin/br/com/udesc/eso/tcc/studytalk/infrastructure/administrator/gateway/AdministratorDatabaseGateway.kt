package br.com.udesc.eso.tcc.studytalk.infrastructure.administrator.gateway

import br.com.udesc.eso.tcc.studytalk.entity.administrator.gateway.AdministratorGateway
import br.com.udesc.eso.tcc.studytalk.entity.administrator.model.Administrator
import br.com.udesc.eso.tcc.studytalk.infrastructure.administrator.gateway.converter.convert
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.administrator.AdministratorRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.administrator.AdministratorSchema
import org.springframework.stereotype.Component

@Component
class AdministratorDatabaseGateway(private val administratorRepository: AdministratorRepository) :
    AdministratorGateway {
    override fun create(uid: String) {
        administratorRepository.save(AdministratorSchema(uid = uid))
    }

    override fun getByUid(uid: String): Administrator? {
        return administratorRepository.findByUid(uid)?.let {
            convert(it)
        }
    }
}