package br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.converter

import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.entity.AdministratorRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.model.AdministratorApiModel
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.model.Administrator

fun convertToModel(administrator: AdministratorRoomEntity): Administrator {
    return Administrator(
        id = administrator.id,
        uid = administrator.uid
    )
}

fun convertToModel(administrator: AdministratorApiModel): Administrator {
    return Administrator(
        id = administrator.id,
        uid = administrator.uid
    )
}

fun convertToRoomEntity(administrator: AdministratorApiModel): AdministratorRoomEntity {
    return AdministratorRoomEntity(
        id = administrator.id,
        uid = administrator.uid
    )
}