package br.com.udesc.eso.tcc.studytalk.featureReport.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.udesc.eso.tcc.studytalk.core.domain.model.Postable

@Entity(tableName = "report")
class ReportRoomEntity(
    @PrimaryKey
    val id: Long,
    val postable: Postable,
    val description: String,
    @ColumnInfo(name = "institution_id")
    val institutionId: Long
)