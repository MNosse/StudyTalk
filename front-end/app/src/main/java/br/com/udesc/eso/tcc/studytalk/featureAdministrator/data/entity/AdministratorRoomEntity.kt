package br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "administrator")
data class AdministratorRoomEntity(
    @PrimaryKey
    val id: Long,
    val uid: String
)