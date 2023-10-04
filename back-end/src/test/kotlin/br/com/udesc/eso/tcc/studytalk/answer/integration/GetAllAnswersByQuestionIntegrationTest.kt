package br.com.udesc.eso.tcc.studytalk.answer.integration

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.GetAllAnswersByQuestionController
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.answer.AnswerRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer.AnswerSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GetAllAnswersByQuestionIntegrationTest @Autowired constructor(
    private val answerRepository: AnswerRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository,
    private val getAllAnswersByQuestionController: GetAllAnswersByQuestionController
) {
    var answer1Description = "Resposta 1"
    var answer2Description = "Resposta 2"
    var participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    var participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"


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
        val participant2 = participantRepository.save(
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
                title = "Pergunta 1",
                description = "Descrição da pergunta 1",
                subjects = mutableListOf(Subject.ARTS),
                participant = participant1,
                institution = institution1
            )
        )
        answerRepository.save(
            AnswerSchema(
                description = answer1Description,
                question = question,
                participant = participant1
            )
        )
        answerRepository.save(
            AnswerSchema(
                description = answer2Description,
                question = question,
                participant = participant1
            )
        )
    }

    @Test
    fun withValidValues() {
        assertDoesNotThrow {
            val response = getAllAnswersByQuestionController.getAllAnswersByQuestion(
                id = 1L,
                participantUid = participant2Uid
            )
            for (answer in response.answers) {
                assert(
                    (answer.id == 1L && answer.description == answer1Description)
                            || (answer.id == 2L && answer.description == answer2Description)
                )
            }
        }
    }

    @Test
    fun withNonExistentParticipant() {
        assertThrows<ParticipantNotFoundException> {
            getAllAnswersByQuestionController.getAllAnswersByQuestion(
                id = 1L,
                participantUid = participant2Uid + "a"
            )
        }
    }

    @Test
    fun withNonExistentQuestion() {
        assertThrows<QuestionNotFoundException> {
            getAllAnswersByQuestionController.getAllAnswersByQuestion(
                id = 2L,
                participantUid = participant2Uid
            )
        }
    }

    @Test
    fun withoutPermission() {
        assertThrows<ParticipantWithoutPermissionException> {
            getAllAnswersByQuestionController.getAllAnswersByQuestion(
                id = 1L,
                participantUid = participant2Uid
            )
        }
    }
}