package br.com.udesc.eso.tcc.studytalk.report.integration

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.answer.AnswerRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.report.ReportRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer.AnswerSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.report.controller.CreateReportController
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CreateReportIntegrationTest @Autowired constructor(
    private val answerRepository: AnswerRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository,
    private val reportRepository: ReportRepository,
    private val createReportController: CreateReportController
) {
    var participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    var participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"
    var postableId = 0L
    val answerDescription = "Descrição da resposta"
    val questionDescription = "Descrição da pergunta"
    val reportDescription = "Descrição da denúncia"

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        val institution1 = institutionRepository.save(InstitutionSchema(name = "Instituição 1"))
        val institution2 = institutionRepository.save(InstitutionSchema(name = "Instituição 2"))
        val participant1 = participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = "Mateus Nosse",
                institution = institution1
            )
        )

        participantRepository.save(
            ParticipantSchema(
                uid = participant2Uid,
                name = "Mateus Coelho",
                institution = when (testInfo.displayName) {
                    "withoutPermission()" -> institution2
                    else -> institution1
                }
            )
        )

        val question = questionRepository.save(
            QuestionSchema(
                title = "Pergunta",
                description = questionDescription,
                subjects = mutableListOf(Subject.MATHEMATICS),
                participant = participant1,
                institution = participant1.institution!!
            )
        )

        postableId = when (testInfo.displayName) {
            "withValidValues2()" -> {
                val answer = answerRepository.save(
                    AnswerSchema(
                        description = answerDescription,
                        question = question,
                        participant = participant1
                    )
                )
                answer.id
            }

            else -> {
                question.id
            }
        }
    }

    @Test
    fun withValidValues1() {
        assertDoesNotThrow {
            createReportController.createReport(
                CreateReportController.Request(
                    requestingParticipantUid = participant2Uid,
                    postableId = postableId,
                    postableType = "QUESTION",
                    description = reportDescription
                )
            )
            val report = reportRepository.findById(1L)
            assert(
                report.isPresent
                        && report.get().id == 1L
                        && report.get().postable.getPostDescription() == questionDescription
                        && report.get().description == reportDescription
            )
        }
    }

    @Test
    fun withValidValues2() {
        assertDoesNotThrow {
            createReportController.createReport(
                CreateReportController.Request(
                    requestingParticipantUid = participant2Uid,
                    postableId = postableId,
                    postableType = "ANSWER",
                    description = reportDescription
                )
            )
            val report = reportRepository.findById(1L)
            assert(
                report.isPresent
                        && report.get().id == 1L
                        && report.get().postable.getPostDescription() == answerDescription
                        && report.get().description == reportDescription
            )
        }
    }

    @Test
    fun withInvalidAnswerId() {
        assertThrows<AnswerNotFoundException> {
            createReportController.createReport(
                CreateReportController.Request(
                    requestingParticipantUid = participant2Uid,
                    postableId = 50L,
                    postableType = "ANSWER",
                    description = reportDescription
                )
            )
        }
    }

    @Test
    fun withInvalidQuestionId() {
        assertThrows<QuestionNotFoundException> {
            createReportController.createReport(
                CreateReportController.Request(
                    requestingParticipantUid = participant2Uid,
                    postableId = 50L,
                    postableType = "QUESTION",
                    description = reportDescription
                )
            )
        }
    }

    @Test
    fun withoutPermission() {
        assertThrows<ParticipantWithoutPermissionException> {
            createReportController.createReport(
                CreateReportController.Request(
                    requestingParticipantUid = participant2Uid,
                    postableId = postableId,
                    postableType = "QUESTION",
                    description = reportDescription
                )
            )
        }
    }

    @Test
    fun withInvalidRequestingParticipantUid() {
        assertThrows<ParticipantNotFoundException> {
            createReportController.createReport(
                CreateReportController.Request(
                    requestingParticipantUid = participant2Uid + "a",
                    postableId = postableId,
                    postableType = "QUESTION",
                    description = reportDescription
                )
            )
        }
    }
}