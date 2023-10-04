package com.example.studytalk.data.entity

data class ParticipantPosts(
    private val ID: Long,
    private val PARTICIPANT : Participant,
    private val POSTS : MutableList<Question> = mutableListOf()
) {
    fun addPost(question: Question) {
        POSTS.add(question)
    }

    fun removePost(question: Question) {
        POSTS.remove(question)
    }
}