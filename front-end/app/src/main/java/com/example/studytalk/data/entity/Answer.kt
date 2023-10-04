package com.example.studytalk.data.entity

import com.example.studytalk.data.interfaces.Postable

data class Answer(
    private val ID: Long,
    private var description: String,
    private var likeCount: Int = 0,
) : Postable {
    fun increaseLikeCount() {
        likeCount++
    }

    fun decreaseLikeCount() {
        likeCount--
    }

    override fun getDescription(): String {
        return description
    }
}