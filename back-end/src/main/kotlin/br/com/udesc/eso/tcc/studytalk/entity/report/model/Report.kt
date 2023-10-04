package br.com.udesc.eso.tcc.studytalk.entity.report.model

import br.com.udesc.eso.tcc.studytalk.core.deserializer.PostableDeserializer
import br.com.udesc.eso.tcc.studytalk.core.interfaces.Postable
import br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

data class Report(
    val id: Long = 0L,
    @JsonDeserialize(using = PostableDeserializer::class)
    val postable: Postable,
    val description: String,
    val institution: Institution? = null
)