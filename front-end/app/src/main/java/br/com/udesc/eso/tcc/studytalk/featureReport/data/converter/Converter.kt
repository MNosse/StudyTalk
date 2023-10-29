package br.com.udesc.eso.tcc.studytalk.featureReport.data.converter

import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.entity.InstitutionRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureReport.data.entity.ReportRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureReport.data.model.ReportApiModel
import br.com.udesc.eso.tcc.studytalk.featureReport.domain.model.Report

fun convertToModel(
    report: ReportRoomEntity,
    institution: InstitutionRoomEntity
): Report {
    return Report(
        id = report.id,
        postable = report.postable,
        description = report.description,
        institution = br.com.udesc.eso.tcc.studytalk.featureInstitution.data.converter.convertToModel(
            institution
        )

    )
}

fun convertToModel(report: ReportApiModel): Report {
    return Report(
        id = report.id,
        postable = report.postable,
        description = report.description,
        institution = br.com.udesc.eso.tcc.studytalk.featureInstitution.data.converter.convertToModel(
            report.institution
        )
    )
}

fun convertToRoomEntity(report: ReportApiModel): ReportRoomEntity {
    return ReportRoomEntity(
        id = report.id,
        postable = report.postable,
        description = report.description,
        institutionId = report.institution.id
    )
}