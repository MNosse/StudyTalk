package br.com.udesc.eso.tcc.studytalk.featureParticipant.data.dataSource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.entity.ParticipantRoomEntity

@Dao
interface LocalParticipantDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(participantRoomEntity: ParticipantRoomEntity)

    @Query("DELETE FROM participant WHERE id = :id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM participant WHERE (SELECT CASE WHEN EXISTS (SELECT 1 FROM administrator WHERE uid = :administratorUid) THEN 1 ELSE 0 END) = 1")
    suspend fun getAll(administratorUid: String): MutableList<ParticipantRoomEntity>

    @Query("SELECT id FROM participant")
    suspend fun getAllIds(): MutableList<Long>

    @Query(
        "SELECT * FROM participant WHERE " +
                "(CASE WHEN :isAdministrator = 1 THEN " +
                "EXISTS (SELECT 1 FROM administrator WHERE uid = :requestingUid) " +
                "ELSE " +
                "EXISTS (SELECT 1 FROM participant WHERE uid = :requestingUid) " +
                "END) = 1 " +
                "AND institution_id = :institutionId"
    )
    suspend fun getAllByInstitution(
        institutionId: Long,
        requestingUid: String,
        isAdministrator: Boolean
    ): MutableList<ParticipantRoomEntity>

    @Query("SELECT * FROM participant WHERE id = :id")
    suspend fun getByIdRelationship(id: Long): ParticipantRoomEntity?

    @Query(
        "SELECT * FROM participant WHERE " +
                "(CASE WHEN :isAdministrator = 1 THEN " +
                "EXISTS (SELECT 1 FROM administrator WHERE uid = :requestingUid) " +
                "ELSE " +
                "EXISTS (SELECT 1 FROM participant WHERE uid = :requestingUid) " +
                "END) = 1 " +
                "AND uid = :participantToBeRetrievedUid"
    )
    suspend fun getByUid(
        participantToBeRetrievedUid: String,
        requestingUid: String,
        isAdministrator: Boolean
    ): ParticipantRoomEntity?

}