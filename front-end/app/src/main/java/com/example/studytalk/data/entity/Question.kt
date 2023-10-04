package com.example.studytalk.data.entity

import com.example.studytalk.data.interfaces.Postable
import com.example.studytalk.data.enumeration.Subject

data class Question(
    private val ID: Long,
    private var title: String,
    private var description: String,
    private val SUBJECTS : MutableList<Subject>
) : Postable {
    fun addSubject(subject: Subject) {
        SUBJECTS.add(subject)
    }

    fun removeSubject(subject: Subject) {
        SUBJECTS.remove(subject)
    }

    override fun getDescription(): String {
        return description
    }
}