package br.com.udesc.eso.tcc.studytalk.entity.administrator.gateway

import br.com.udesc.eso.tcc.studytalk.entity.administrator.model.Administrator

interface AdministratorGateway {
    fun create(uid: String)
    fun getByUid(uid: String): Administrator?
}