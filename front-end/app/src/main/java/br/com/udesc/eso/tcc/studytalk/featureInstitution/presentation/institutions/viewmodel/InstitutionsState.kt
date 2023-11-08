package br.com.udesc.eso.tcc.studytalk.featureInstitution.presentation.institutions.viewmodel

import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.model.Institution

data class InstitutionsState(
    val institutions: List<Institution> = emptyList()
)
