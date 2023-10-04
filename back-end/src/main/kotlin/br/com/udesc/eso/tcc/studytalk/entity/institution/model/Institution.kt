package br.com.udesc.eso.tcc.studytalk.entity.institution.model

data class Institution(
    val id: Long = 0L,
    val registrationCode: String? = null,
    var name: String
)