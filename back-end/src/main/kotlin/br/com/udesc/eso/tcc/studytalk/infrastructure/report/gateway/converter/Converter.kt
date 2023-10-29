package br.com.udesc.eso.tcc.studytalk.infrastructure.report.gateway.converter

import br.com.udesc.eso.tcc.studytalk.core.interfaces.Postable
import br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer
import br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution
import br.com.udesc.eso.tcc.studytalk.entity.question.model.Question
import br.com.udesc.eso.tcc.studytalk.entity.report.model.Report
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer.AnswerSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.report.ReportSchema

fun convert(reportSchema: ReportSchema): Report {
    return Report(
        id = reportSchema.id,
        postable = convert(reportSchema.postable),
        description = reportSchema.description,
        institution = convert(reportSchema.institution)
    )
}

private fun convert(postable: Postable): Postable {
    return when (postable) {
        is QuestionSchema -> {
            Question(
                id = postable.id,
                title = postable.title,
                description = postable.description,
                subjects = postable.subjects
            )
        }

        else -> {
            val answer = postable as AnswerSchema
            Answer(
                id = answer.id,
                description = answer.description,
                likeCount = answer.likeCount
            )
        }
    }
}

private fun convert(institutionSchema: InstitutionSchema): Institution {
    return Institution(
        id = institutionSchema.id,
        name = institutionSchema.name,
        registrationCode = institutionSchema.registrationCode
    )
}