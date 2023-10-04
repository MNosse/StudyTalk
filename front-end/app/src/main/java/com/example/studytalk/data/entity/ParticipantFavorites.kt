package com.example.studytalk.data.entity

data class ParticipantFavorites(
    private val ID: Long,
    private val PARTICIPANT : Participant,
    private val FAVORITES : MutableList<Question> = mutableListOf()
) {
    fun addFavorite(question: Question) {
        FAVORITES.add(question)
    }

    fun removeFavorite(question: Question) {
        FAVORITES.remove(question)
    }
}