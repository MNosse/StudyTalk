package br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.repository

import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.model.Institution

interface InstitutionRepository {

    suspend fun create(administratorUid: String, name: String): Result<Unit>

    suspend fun delete(id: Long, administratorUid: String): Result<Unit>

    suspend fun getAll(administratorUid: String): Result<MutableList<Institution>>

    suspend fun getById(
        id: Long,
        requestingUid: String,
        isAdministrator: Boolean
    ): Result<Institution?>

    suspend fun getByRegistrationCode(
        registrationCode: String,
        requestingUid: String,
        isAdministrator: Boolean
    ): Result<Institution?>

    suspend fun update(
        id: Long,
        administratorUid: String,
        name: String
    ): Result<Unit>

}