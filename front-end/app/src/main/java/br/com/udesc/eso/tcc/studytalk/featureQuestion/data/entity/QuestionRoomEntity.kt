package br.com.udesc.eso.tcc.studytalk.featureQuestion.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.udesc.eso.tcc.studytalk.core.domain.model.Postable
import br.com.udesc.eso.tcc.studytalk.core.domain.model.Subject

@Entity(tableName = "question")
data class QuestionRoomEntity(
    @PrimaryKey
    val id: Long,
    var title: String,
    var description: String,
    val subjects: MutableList<Subject>,
    @ColumnInfo(name = "participant_id")
    val participantId: Long,
    @ColumnInfo(name = "institution_id")
    val institutionId: Long
) : Postable {
    override fun getPostDescription(): String {
        return description
    }
}