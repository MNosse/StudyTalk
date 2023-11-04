package br.com.udesc.eso.tcc.studytalk.featureReport.domain.repository

import br.com.udesc.eso.tcc.studytalk.featureReport.domain.model.Report

interface ReportRepository {

    suspend fun approve(id: Long, approverUid: String): Result<Unit>

    suspend fun create(
        requestingParticipantUid: String,
        postableId: Long,
        postableType: String,
        description: String
    ): Result<Unit>

    suspend fun getAllByInstitution(
        id: Long,
        requestingParticipantUid: String
    ): Result<MutableList<Report>>

    suspend fun getById(
        id: Long,
        requestingParticipantUid: String
    ): Result<Report?>

    suspend fun reprove(id: Long, reproverUid: String): Result<Unit>

}