package br.com.udesc.eso.tcc.studytalk.infrastructure.report.gateway

import br.com.udesc.eso.tcc.studytalk.core.interfaces.Postable
import br.com.udesc.eso.tcc.studytalk.entity.report.gateway.ReportGateway
import br.com.udesc.eso.tcc.studytalk.entity.report.model.Report
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.answer.AnswerRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.report.ReportRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer.AnswerSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.report.ReportSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.report.gateway.converter.convert
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class ReportDatabaseGateway(
    private val answerRepository: AnswerRepository,
    private val questionRepository: QuestionRepository,
    private val reportRepository: ReportRepository
) : ReportGateway {
    override fun approve(id: Long) {
        reportRepository.findById(id).getOrNull()?.let {
            when (it.postable) {
                is AnswerSchema -> {
                    answerRepository.delete(it.postable)
                }

                is QuestionSchema -> {
                    questionRepository.delete(it.postable)
                }
            }
            reportRepository.delete(it)
        }
    }

    override fun create(postableId: Long, postableType: String, description: String) {
        var institution: InstitutionSchema? = null
        var postable: Postable? = null
        when (postableType) {
            "ANSWER" -> {
                answerRepository.findById(postableId).getOrNull()?.let {
                    postable = it
                    institution = it.question.institution
                }
            }

            "QUESTION" -> {
                questionRepository.findById(postableId).getOrNull()?.let {
                    postable = it
                    institution = it.institution
                }
            }
        }
        postable?.let {
            reportRepository.save(
                ReportSchema(
                    postable = it,
                    description = description,
                    institution = institution!!
                )
            )
        }
    }

    override fun getAllByInstitutionId(id: Long): MutableList<Report> {
        return reportRepository.findAllByInstitutionId(id).map {
            convert(it)
        }.toMutableList()
    }

    override fun getById(id: Long): Report? {
        return reportRepository.findById(id).getOrNull()?.let {
            convert(it)
        }
    }

    override fun reprove(id: Long) {
        reportRepository.findById(id).getOrNull()?.let {
            reportRepository.delete(it)
        }
    }
}