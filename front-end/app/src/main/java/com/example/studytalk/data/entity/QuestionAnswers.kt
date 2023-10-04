package com.example.studytalk.data.entity

data class QuestionAnswers(
    private val ID: Long,
    private val QUESTION: Question,
    private val ANSWERS: MutableList<Answer> = mutableListOf()
)
