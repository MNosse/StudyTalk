package br.com.udesc.eso.tcc.studytalk.core.data.dataBase

import androidx.room.TypeConverter
import br.com.udesc.eso.tcc.studytalk.core.domain.model.Postable
import br.com.udesc.eso.tcc.studytalk.core.domain.model.Subject
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun fromLongList(list: MutableList<Long>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toLongList(value: String): MutableList<Long> {
        return value.split(",").map { it.toLong() }.toMutableList()
    }

    @TypeConverter
    fun fromSubjectList(list: MutableList<Subject>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toSubjectList(value: String): MutableList<Subject> {
        return value.split(",").map { Subject.valueOf(it) }.toMutableList()
    }

    @TypeConverter
    fun fromPostable(postable: Postable): String {
        return Gson().toJson(postable)
    }

    @TypeConverter
    fun toPostable(json: String): Postable {
        return Gson().fromJson(json, Postable::class.java)
    }
}