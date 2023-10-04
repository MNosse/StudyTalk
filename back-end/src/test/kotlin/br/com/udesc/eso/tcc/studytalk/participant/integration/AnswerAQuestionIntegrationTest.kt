package br.com.udesc.eso.tcc.studytalk.participant.integration

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.answer.AnswerRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.AnswerAQuestionController
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.DoAQuestionController
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.jvm.optionals.getOrNull

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AnswerAQuestionIntegrationTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository,
    private val answerRepository: AnswerRepository,
    private val answerAQuestionController: AnswerAQuestionController
) {
    val answerDescription = "Descrição da Resposta"
    val participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        val institution = institutionRepository.save(InstitutionSchema(name = "Instituição 1"))
        val participant = participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = "Mateus Nosse",
                institution = institution
            )
        )
        questionRepository.save(
            QuestionSchema(
                title = "Questão",
                description = "Descrição da Questão",
                subjects = mutableListOf(Subject.MATHEMATICS, Subject.SCIENCE),
                participant = participant,
                institution = institution
            )
        )
    }

    @Test
    fun withValidValues() {
        assertDoesNotThrow {
            answerAQuestionController.answerAQuestion(
                participantUid = participant1Uid,
                questionId = 1L,
                AnswerAQuestionController.Request(
                    description = answerDescription,
                )
            )
            answerRepository.findById(1L).getOrNull()?.let {
                assert(it.id == 1L && it.description == answerDescription)
            }
        }
    }

    @Test
    fun withBlankValues() {
        assertThrows<ConstraintViolationException> {
            answerAQuestionController.answerAQuestion(
                participantUid = participant1Uid,
                questionId = 1L,
                AnswerAQuestionController.Request(
                    description = " ",
                )
            )
        }
    }

    @Test
    fun withOverflowedDescription() {
        assertThrows<ConstraintViolationException> {
            answerAQuestionController.answerAQuestion(
                participantUid = participant1Uid,
                questionId = 1L,
                AnswerAQuestionController.Request(
                    description = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw",
                )
            )
        }
    }

    @Test
    fun withNonExistentParticipant() {
        assertThrows<ParticipantNotFoundException> {
            answerAQuestionController.answerAQuestion(
                participantUid = participant1Uid + "a",
                questionId = 1L,
                AnswerAQuestionController.Request(
                    description = answerDescription,
                )
            )
        }
    }

    @Test
    fun withNonExistentQuestion() {
        assertThrows<QuestionNotFoundException> {
            answerAQuestionController.answerAQuestion(
                participantUid = participant1Uid,
                questionId = 2L,
                AnswerAQuestionController.Request(
                    description = answerDescription,
                )
            )
        }
    }
}