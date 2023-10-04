package br.com.udesc.eso.tcc.studytalk.core.enums

enum class Privilege(val value: String) {
    PRINCIPAL("Diretor(a)"),
    TEACHER("Professor(a)"),
    DEFAULT("Participante")
}