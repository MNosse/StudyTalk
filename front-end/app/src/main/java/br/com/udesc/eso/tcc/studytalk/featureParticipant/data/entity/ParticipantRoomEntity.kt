package br.com.udesc.eso.tcc.studytalk.featureParticipant.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.udesc.eso.tcc.studytalk.core.domain.model.Privilege

@Entity(tableName = "participant")
data class ParticipantRoomEntity(
    @PrimaryKey
    val id: Long,
    val uid: String,
    var name: String,
    var privilege: Privilege,
    @ColumnInfo(name = "institution_id")
    val institutionId: Long? = null,
    @ColumnInfo(name = "favorite_questions")
    val favoriteQuestions: MutableList<Long> = mutableListOf(),
    @ColumnInfo(name = "liked_answers")
    val likedAnswers: MutableList<Long> = mutableListOf()
)