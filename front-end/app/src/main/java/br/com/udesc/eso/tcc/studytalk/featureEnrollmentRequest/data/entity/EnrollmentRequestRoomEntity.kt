package br.com.udesc.eso.tcc.studytalk.featureEnrollmentRequest.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "enrollment_request")
class EnrollmentRequestRoomEntity(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "institution_id")
    val institutionId: Long,
    @ColumnInfo(name = "participant_id")
    val participantId: Long
)