package br.com.udesc.eso.tcc.studytalk.featureInstitution.data.response

import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.model.InstitutionApiModel

data class GetAllInstitutionsResponse(
    val institutions: MutableList<InstitutionApiModel>
)
