package br.com.udesc.eso.tcc.studytalk.report.integration

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.entity.report.exception.ReportNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.answer.AnswerRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.report.ReportRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer.AnswerSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.report.ReportSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.report.controller.ReproveReportController
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReproveReportIntegrationTest @Autowired constructor(
    private val answerRepository: AnswerRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository,
    private val reportRepository: ReportRepository,
    private val reproveReportController: ReproveReportController
) {
    var participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    var participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"
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
                privilege = when (testInfo.displayName) {
                    "withValidValues2()" -> Privilege.TEACHER
                    "withoutPrivilege()" -> Privilege.DEFAULT
                    else -> Privilege.PRINCIPAL
                },
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

        val postable = when (testInfo.displayName) {
            "withValidValues3()" -> answerRepository.save(
                AnswerSchema(
                    description = answerDescription,
                    question = question,
                    participant = participant1
                )
            )

            else -> question
        }
        reportRepository.save(
            ReportSchema(
                postable = postable,
                description = reportDescription,
                institution = institution1
            )
        )

    }

    @Test
    fun withValidValues1() {
        assertDoesNotThrow {
            reproveReportController.reproveReportById(
                id = 1L,
                reproverUid = participant2Uid
            )
            assert(reportRepository.findById(1L).isEmpty)
        }
    }

    @Test
    fun withValidValues2() {
        assertDoesNotThrow {
            reproveReportController.reproveReportById(
                id = 1L,
                reproverUid = participant2Uid
            )
            assert(reportRepository.findById(1L).isEmpty)
        }
    }

    @Test
    fun withValidValues3() {
        assertDoesNotThrow {
            reproveReportController.reproveReportById(
                id = 1L,
                reproverUid = participant2Uid
            )
            assert(reportRepository.findById(1L).isEmpty)
        }
    }

    @Test
    fun withoutPrivilege() {
        assertThrows<ParticipantWithoutPrivilegeException> {
            reproveReportController.reproveReportById(
                id = 1L,
                reproverUid = participant2Uid
            )
        }
    }

    @Test
    fun withoutPermission() {
        assertThrows<ParticipantWithoutPermissionException> {
            reproveReportController.reproveReportById(
                id = 1L,
                reproverUid = participant2Uid
            )
        }
    }

    @Test
    fun withInvalidId() {
        assertThrows<ReportNotFoundException> {
            reproveReportController.reproveReportById(
                id = 2L,
                reproverUid = participant2Uid
            )
        }
    }

    @Test
    fun withInvalidReproverUid() {
        assertThrows<ParticipantNotFoundException> {
            reproveReportController.reproveReportById(
                id = 1L,
                reproverUid = participant2Uid + "a"
            )
        }
    }
}