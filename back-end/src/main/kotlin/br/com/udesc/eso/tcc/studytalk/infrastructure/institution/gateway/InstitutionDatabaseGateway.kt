package br.com.udesc.eso.tcc.studytalk.infrastructure.institution.gateway

import br.com.udesc.eso.tcc.studytalk.entity.institution.gateway.InstitutionGateway
import br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.institution.gateway.converter.convert
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class InstitutionDatabaseGateway(private val institutionRepository: InstitutionRepository) : InstitutionGateway {
    override fun create(name: String) {
        institutionRepository.save(InstitutionSchema(name = name))
    }

    override fun delete(id: Long) {
        institutionRepository.findById(id).getOrNull()?.let {
            institutionRepository.delete(it)
        }
    }

    override fun getAll(): MutableList<Institution> {
        return institutionRepository.findAll().map {
            convert(it)
        }.toMutableList()
    }

    override fun getById(id: Long): Institution? {
        return institutionRepository.findById(id).getOrNull()?.let {
            convert(it)
        }
    }

    override fun getByRegistrationCode(registrationCode: String): Institution? {
        return institutionRepository.findByRegistrationCode(registrationCode)?.let {
            convert(it)
        }
    }

    override fun update(id: Long, name: String) {
        institutionRepository.findById(id).getOrNull()?.let {
            it.name = name
            institutionRepository.save(it)
        }
    }
}