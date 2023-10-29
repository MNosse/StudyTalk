package br.com.udesc.eso.tcc.studytalk.featureReport.domain.model

import br.com.udesc.eso.tcc.studytalk.core.domain.model.Postable
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.model.Institution

data class Report(
    val id: Long = 0L,
    val postable: Postable,
    val description: String,
    val institution: Institution
)