package com.example.studytalk.data.entity

data class InstitutionParticipants(
    private val ID: Long,
    private val INSTITUTION : Institution,
    private val PARTICIPANTS: MutableList<Participant> = mutableListOf()
) {
    fun addParticipant(participant: Participant) {
        PARTICIPANTS.add(participant)
    }

    fun removeParticipant(participant: Participant) {
        PARTICIPANTS.remove(participant)
    }
}