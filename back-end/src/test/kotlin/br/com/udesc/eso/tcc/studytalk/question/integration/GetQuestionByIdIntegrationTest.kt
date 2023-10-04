package br.com.udesc.eso.tcc.studytalk.question.integration

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.answer.AnswerRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.GetQuestionByIdController
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GetQuestionByIdIntegrationTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository,
    private val getQuestionByIdController: GetQuestionByIdController
) {
    val questionTitle = "Questão 1"
    val questionDescription = "Descrição da Questão 1"
    val questionSubjects = mutableListOf(Subject.MATHEMATICS, Subject.SCIENCE)
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
                title = questionTitle,
                description = questionDescription,
                subjects = questionSubjects,
                participant = participant1,
                institution = institution1
            )
        )
    }

    @Test
    fun withValidValues() {
        assertDoesNotThrow {
            val response = getQuestionByIdController.getQuestionById(
                id = 1L,
                participantUid = participant1Uid
            )
            assert(
                response.id == 1L
                        && response.title == questionTitle
                        && response.description == questionDescription
                        && response.subjects == questionSubjects
            )
        }
    }

    @Test
    fun withInvalidId() {
        assertThrows<QuestionNotFoundException> {
            getQuestionByIdController.getQuestionById(
                id = 2L,
                participantUid = participant1Uid
            )
        }
    }

    @Test
    fun withNonExistentParticipant() {
        assertThrows<ParticipantNotFoundException> {
            getQuestionByIdController.getQuestionById(
                id = 1L,
                participantUid = participant1Uid + "a"
            )
        }
    }

    @Test
    fun withoutPermission() {
        assertThrows<ParticipantWithoutPermissionException> {
            getQuestionByIdController.getQuestionById(
                id = 1L,
                participantUid = participant2Uid
            )
        }
    }
}