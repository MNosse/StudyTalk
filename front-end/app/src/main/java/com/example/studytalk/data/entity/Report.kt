package com.example.studytalk.data.entity

import com.example.studytalk.data.interfaces.Postable

data class Report(
    private val ID: Long,
    private val POSTABLE : Postable,
    private val DESCRIPTION: String
)
