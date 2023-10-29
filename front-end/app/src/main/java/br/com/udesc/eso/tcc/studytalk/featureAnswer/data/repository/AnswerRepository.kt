package br.com.udesc.eso.tcc.studytalk.featureAnswer.data.repository

import br.com.udesc.eso.tcc.studytalk.featureAnswer.domain.model.Answer

interface AnswerRepository {

    suspend fun delete(id: Long, participantUid: String): Result<Unit>

    suspend fun getAllByQuestion(
        id: Long,
        participantUid: String
    ): Result<MutableList<Answer>>

    suspend fun getById(
        id: Long,
        participantUid: String
    ): Result<Answer?>

    suspend fun update(
        id: Long,
        description: String,
        participantUid: String
    ): Result<Unit>

}