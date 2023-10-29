package br.com.udesc.eso.tcc.studytalk.featureReport.data.model

import br.com.udesc.eso.tcc.studytalk.core.domain.model.Postable
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.model.InstitutionApiModel

class ReportApiModel(
    val id: Long,
    val postable: Postable,
    val description: String,
    val institution: InstitutionApiModel
)