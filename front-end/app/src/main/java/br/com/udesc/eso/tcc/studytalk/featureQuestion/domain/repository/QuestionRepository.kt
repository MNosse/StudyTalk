package br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.repository

import br.com.udesc.eso.tcc.studytalk.core.domain.model.Subject
import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.model.Question

interface QuestionRepository {

    suspend fun delete(id: Long, participantUid: String): Result<Unit>

    suspend fun getAllByInstitution(
        id: Long,
        participantUid: String
    ): Result<MutableList<Question>>

    suspend fun getById(
        id: Long,
        participantUid: String
    ): Result<Question?>

    suspend fun update(
        id: Long,
        title: String?,
        description: String?,
        subjects: MutableList<Subject>?,
        participantUid: String
    ): Result<Unit>

}