package br.com.udesc.eso.tcc.studytalk.question.integration

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.GetAllQuestionsByInstitutionController
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GetAllQuestionsByInstitutionIntegrationTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository,
    private val getAllQuestionsByQuestionController: GetAllQuestionsByInstitutionController
) {
    val question1Title = "Questão 1"
    val question1Description = "Descrição da Questão 1"
    val question1Subjects = mutableListOf(Subject.MATHEMATICS, Subject.SCIENCE)
    val question2Title = "Questão 2"
    val question2Description = "Descrição da Questão 2"
    val question2Subjects = mutableListOf(Subject.PHILOSOPHY, Subject.SOCIOLOGY)
    val participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    val participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"


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
        questionRepository.save(
            QuestionSchema(
                title = question1Title,
                description = question1Description,
                subjects = question1Subjects,
                participant = participant1,
                institution = institution1
            )
        )
        questionRepository.save(
            QuestionSchema(
                title = question2Title,
                description = question2Description,
                subjects = question2Subjects,
                participant = participant1,
                institution = institution1
            )
        )
    }

    @Test
    fun withValidValues() {
        assertDoesNotThrow {
            val response = getAllQuestionsByQuestionController.getAllQuestionsByInstitutionId(
                id = 1L,
                participantUid = participant1Uid
            )
            for (question in response.questions) {
                assert(
                    (question.id == 1L && question.title == question1Title && question.description == question1Description && question.subjects == question1Subjects)
                            || (question.id == 2L && question.title == question2Title && question.description == question2Description && question.subjects == question2Subjects)
                )
            }
        }
    }

    @Test
    fun withNonExistentParticipant() {
        assertThrows<ParticipantNotFoundException> {
            getAllQuestionsByQuestionController.getAllQuestionsByInstitutionId(
                id = 1L,
                participantUid = participant2Uid + "a"
            )
        }
    }

    @Test
    fun withoutPermission() {
        assertThrows<ParticipantWithoutPermissionException> {
            getAllQuestionsByQuestionController.getAllQuestionsByInstitutionId(
                id = 1L,
                participantUid = participant2Uid
            )
        }
    }
}