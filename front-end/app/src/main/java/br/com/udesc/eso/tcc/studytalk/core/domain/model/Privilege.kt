package br.com.udesc.eso.tcc.studytalk.core.domain.model

enum class Privilege(val value: String) {
    PRINCIPAL("Diretor(a)"),
    TEACHER("Professor(a)"),
    DEFAULT("Participante")
}