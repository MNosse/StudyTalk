package br.com.udesc.eso.tcc.studytalk.featureAnswer.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.udesc.eso.tcc.studytalk.core.domain.model.Postable

@Entity(tableName = "answer")
class AnswerRoomEntity(
    @PrimaryKey
    val id: Long,
    var description: String,
    var likeCount: Int,
    @ColumnInfo(name = "question_id")
    val questionId: Long,
    @ColumnInfo(name = "participant_id")
    val participantId: Long
) : Postable {
    override fun getPostDescription(): String {
        return description
    }
}