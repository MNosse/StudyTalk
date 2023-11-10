package br.com.udesc.eso.tcc.studytalk.featureInstitution.data.converter

import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.entity.InstitutionRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.model.InstitutionApiModel
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.model.Institution

fun convertToModel(institution: InstitutionRoomEntity): Institution {
    return Institution(
        id = institution.id,
        registrationCode = institution.registrationCode,
        name = institution.name
    )
}

fun convertToModel(institution: InstitutionApiModel?): Institution? {
    return institution?.let {
        Institution(
            id = institution.id,
            registrationCode = institution.registrationCode,
            name = institution.name
        )
    }
}

fun convertToRoomEntity(institution: InstitutionApiModel): InstitutionRoomEntity {
    return InstitutionRoomEntity(
        id = institution.id,
        registrationCode = institution.registrationCode,
        name = institution.name
    )
}