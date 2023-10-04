package br.com.udesc.eso.tcc.studytalk.entity.institution.gateway

import br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution

interface InstitutionGateway {
    fun create(name: String)
    fun delete(id: Long)
    fun getAll(): MutableList<Institution>
    fun getById(id: Long): Institution?
    fun getByRegistrationCode(registrationCode: String): Institution?
    fun update(id: Long, name: String)
}