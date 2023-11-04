package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.repository

import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.domain.model.EnrollmentRequest

interface EnrollmentRequestRepository {

    suspend fun approve(id: Long, approverUid: String): Result<Unit>

    suspend fun getAllByInstitution(
        id: Long,
        requestingParticipantUid: String
    ): Result<MutableList<EnrollmentRequest>>

    suspend fun getById(
        id: Long,
        requestingParticipantUid: String
    ): Result<EnrollmentRequest?>

    suspend fun getByParticipantId(
        id: Long,
        requestingParticipantUid: String
    ): Result<EnrollmentRequest?>

    suspend fun reprove(id: Long, reproverUid: String): Result<Unit>

}