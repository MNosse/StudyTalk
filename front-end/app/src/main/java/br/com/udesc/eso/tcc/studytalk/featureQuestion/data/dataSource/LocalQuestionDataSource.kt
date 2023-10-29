package br.com.udesc.eso.tcc.studytalk.featureQuestion.data.dataSource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.entity.QuestionRoomEntity

@Dao
interface LocalQuestionDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(questionRoomEntity: QuestionRoomEntity)

    @Query("DELETE FROM question WHERE id = :id")
    fun deleteById(id: Long)

    @Query(
        "SELECT * FROM question WHERE " +
                "(SELECT CASE WHEN EXISTS (SELECT 1 FROM participant WHERE uid = :participantUid) THEN 1 ELSE 0 END) = 1 " +
                "AND institution_id = :id"
    )
    suspend fun getAllByInstitution(
        id: Long,
        participantUid: String
    ): MutableList<QuestionRoomEntity>

    @Query ("SELECT id FROM question")
    suspend fun getAllIds(): MutableList<Long>

    @Query(
        "SELECT * FROM question WHERE " +
                "(SELECT CASE WHEN EXISTS (SELECT 1 FROM participant WHERE uid = :participantUid) THEN 1 ELSE 0 END) = 1 " +
                "AND id = :id"
    )
    suspend fun getById(
        id: Long,
        participantUid: String
    ): QuestionRoomEntity?

    @Query("SELECT * FROM question WHERE id = :id")
    suspend fun getByIdRelationship(id: Long): QuestionRoomEntity?

}