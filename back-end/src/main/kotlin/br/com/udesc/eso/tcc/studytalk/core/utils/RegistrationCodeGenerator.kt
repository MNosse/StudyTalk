package br.com.udesc.eso.tcc.studytalk.core.utils

fun generateCode(): String {
    val letras = ('a'..'z') + ('A'..'Z')
    return (1..8)
        .map { letras.random() }
        .joinToString("")
}