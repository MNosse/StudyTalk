package br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.repository

import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.model.Administrator

interface AdministratorRepository {

    suspend fun create(uid: String): Result<Unit>

    suspend fun getAdministratorByUid(uid: String): Result<Administrator?>

}