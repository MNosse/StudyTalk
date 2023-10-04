package com.example.studytalk.data.entity

import com.example.studytalk.data.enumeration.Privilege

data class Participant(
    private val ID: Long,
    private var name: String,
    private var privilege: Privilege = Privilege.DEFAULT,
    private val INSTITUTION: Institution,
)