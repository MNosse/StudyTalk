package br.com.udesc.eso.tcc.studytalk.featureReport.data.dataSource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.udesc.eso.tcc.studytalk.featureReport.data.entity.ReportRoomEntity
import retrofit2.http.Path

@Dao
interface LocalReportDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(reportRoomEntity: ReportRoomEntity)

    @Query("DELETE FROM report WHERE id = :id")
    fun deleteById(id: Long)

    @Query ("SELECT id FROM report")
    suspend fun getAllIds(): MutableList<Long>

    @Query(
        "SELECT * FROM report WHERE " +
                "(SELECT CASE WHEN EXISTS (SELECT 1 FROM participant WHERE uid = :requestingParticipantUid) THEN 1 ELSE 0 END) = 1 " +
                "AND institution_id = :id"
    )
    suspend fun getAllByInstitution(
        @Path("id") id: Long,
        @retrofit2.http.Query("requestingParticipantUid") requestingParticipantUid: String
    ): MutableList<ReportRoomEntity>

    @Query(
        "SELECT * FROM report WHERE " +
                "(SELECT CASE WHEN EXISTS (SELECT 1 FROM participant WHERE uid = :requestingParticipantUid) THEN 1 ELSE 0 END) = 1 " +
                "AND id = :id"
    )
    suspend fun getById(
        id: Long,
        requestingParticipantUid: String
    ): ReportRoomEntity?

}