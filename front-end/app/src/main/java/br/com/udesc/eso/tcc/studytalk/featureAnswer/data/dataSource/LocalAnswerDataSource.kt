package br.com.udesc.eso.tcc.studytalk.featureAnswer.data.dataSource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.entity.AnswerRoomEntity

@Dao
interface LocalAnswerDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(answerRoomEntity: AnswerRoomEntity)

    @Query("DELETE FROM answer WHERE id = :id")
    fun deleteById(id: Long)

    @Query(
        "SELECT * FROM answer WHERE " +
                "(SELECT CASE WHEN EXISTS (SELECT 1 FROM participant WHERE uid = :participantUid) THEN 1 ELSE 0 END) = 1 " +
                "AND question_id = :id"
    )
    suspend fun getAllByQuestion(
        id: Long,
        participantUid: String
    ): MutableList<AnswerRoomEntity>

    @Query("SELECT id FROM answer")
    suspend fun getAllIds(): MutableList<Long>

    @Query(
        "SELECT * FROM answer WHERE " +
                "(SELECT CASE WHEN EXISTS (SELECT 1 FROM participant WHERE uid = :participantUid) THEN 1 ELSE 0 END) = 1 " +
                "AND id = :id"
    )
    suspend fun getById(
        id: Long,
        participantUid: String
    ): AnswerRoomEntity?

    @Query("SELECT * FROM answer WHERE id = :id")
    suspend fun getByIdRelationship(id: Long): AnswerRoomEntity?

}