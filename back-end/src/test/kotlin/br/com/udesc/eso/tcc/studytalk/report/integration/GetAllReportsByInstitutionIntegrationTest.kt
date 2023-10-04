package br.com.udesc.eso.tcc.studytalk.report.integration

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.answer.AnswerRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.enrollmentRequest.EnrollmentRequestRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.report.ReportRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer.AnswerSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.enrollmentRequest.EnrollmentRequestSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.report.ReportSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.GetAllEnrollmentRequestiesByInstitutionController
import br.com.udesc.eso.tcc.studytalk.infrastructure.report.controller.GetAllReportsByInstitutionController
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GetAllReportsByInstitutionIntegrationTest @Autowired constructor(
    private val answerRepository: AnswerRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository,
    private val reportRepository: ReportRepository,
    private val getAllReportsByInstitutionController: GetAllReportsByInstitutionController
) {
    var participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    var participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"
    val answerDescription = "Descrição da resposta"
    val questionDescription = "Descrição da pergunta"
    val answerReportDescription = "Descrição da denúncia da resposta"
    val questionReportDescription = "Descrição da denúncia da pergunta"

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

        val answer = answerRepository.save(
            AnswerSchema(
                description = answerDescription,
                question = question,
                participant = participant1
            )
        )

        reportRepository.save(
            ReportSchema(
                postable = question,
                description = questionReportDescription,
                institution = institution1
            )
        )

        reportRepository.save(
            ReportSchema(
                postable = answer,
                description = answerReportDescription,
                institution = institution1
            )
        )
    }

    @Test
    fun withValidValues1() {
        assertDoesNotThrow {
            val response = getAllReportsByInstitutionController.getAllReportsByInstitutionId(
                id = 1L,
                requestingParticipantUid = participant2Uid
            )
            for (report in response.reports) {
                assert(
                    (report.id == 1L && report.description == questionReportDescription && report.postable.getPostDescription() == questionDescription)
                            || (report.id == 2L && report.description == answerReportDescription && report.postable.getPostDescription() == answerDescription)
                )
            }
        }
    }

    @Test
    fun withValidValues2() {
        assertDoesNotThrow {
            val response = getAllReportsByInstitutionController.getAllReportsByInstitutionId(
                id = 1L,
                requestingParticipantUid = participant2Uid
            )
            for (report in response.reports) {
                assert(
                    (report.id == 1L && report.description == questionReportDescription && report.postable.getPostDescription() == questionDescription)
                            || (report.id == 2L && report.description == answerReportDescription && report.postable.getPostDescription() == answerDescription)
                )
            }
        }
    }

    @Test
    fun withoutPrivilege() {
        assertThrows<ParticipantWithoutPrivilegeException> {
            getAllReportsByInstitutionController.getAllReportsByInstitutionId(
                id = 1L,
                requestingParticipantUid = participant2Uid
            )
        }
    }

    @Test
    fun withoutPermission() {
        assertThrows<ParticipantWithoutPermissionException> {
            getAllReportsByInstitutionController.getAllReportsByInstitutionId(
                id = 1L,
                requestingParticipantUid = participant2Uid
            )
        }
    }

    @Test
    fun withInvalidRequestingParticipantUid() {
        assertThrows<ParticipantNotFoundException> {
            getAllReportsByInstitutionController.getAllReportsByInstitutionId(
                id = 1L,
                requestingParticipantUid = participant2Uid + "a"
            )
        }
    }
}