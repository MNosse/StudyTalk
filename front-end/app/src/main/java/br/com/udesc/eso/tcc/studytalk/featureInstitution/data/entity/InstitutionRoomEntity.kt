package br.com.udesc.eso.tcc.studytalk.featureInstitution.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "institution")
data class InstitutionRoomEntity(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "registration_code")
    val registrationCode: String,
    var name: String
)