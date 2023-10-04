package br.com.udesc.eso.tcc.studytalk.core.enums

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

enum class Subject(val value: String) {
    ARTS("Artes"),
    ENGLISH("Inglês"),
    GEOGRAPHY("Geografia"),
    HISTORY("História"),
    MATHEMATICS("Matemática"),
    PHILOSOPHY("Filosofia"),
    PORTUGUESE("Português"),
    SCIENCE("Ciência"),
    SOCIOLOGY("Sociologia"),
    SPANISH("Espanhol")
}