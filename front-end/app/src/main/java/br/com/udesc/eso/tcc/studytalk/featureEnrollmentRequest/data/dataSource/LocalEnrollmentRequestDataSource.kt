package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.dataSource

import androidx.room.Dao
import androidx.room.Query
import br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.entity.EnrollmentRequestRoomEntity

@Dao
interface LocalEnrollmentRequestDataSource {

    @Query(
        "SELECT * FROM enrollment_request WHERE " +
                "(SELECT CASE WHEN EXISTS (SELECT 1 FROM participant WHERE uid = :requestingParticipantUid) THEN 1 ELSE 0 END) = 1 " +
                "AND institution_id = :id"
    )
    suspend fun getAllByInstitution(
        id: Long,
        requestingParticipantUid: String
    ): MutableList<EnrollmentRequestRoomEntity>

    @Query(
        "SELECT * FROM enrollment_request WHERE " +
                "(SELECT CASE WHEN EXISTS (SELECT 1 FROM participant WHERE uid = :requestingParticipantUid) THEN 1 ELSE 0 END) = 1 " +
                "AND id = :id"
    )
    suspend fun getById(
        id: Long,
        requestingParticipantUid: String
    ): EnrollmentRequestRoomEntity?

    @Query(
        "SELECT * FROM enrollment_request WHERE " +
                "(SELECT CASE WHEN EXISTS (SELECT 1 FROM participant WHERE uid = :requestingParticipantUid) THEN 1 ELSE 0 END) = 1 " +
                "AND participant_id = :id"
    )
    suspend fun getByParticipant(
        id: Long,
        requestingParticipantUid: String
    ): EnrollmentRequestRoomEntity?

}