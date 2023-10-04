package br.com.udesc.eso.tcc.studytalk.answer.integration

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerIsNotFromParticipantException
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.UpdateAnswerController
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.answer.AnswerRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer.AnswerSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
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
class UpdateAnswerIntegrationTest @Autowired constructor(
    private val answerRepository: AnswerRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository,
    private val updateAnswerController: UpdateAnswerController
) {
    var answerDescription = "Descrição da Resposta"
    var updatedAnswerDescription = "Nova descrição da Resposta"
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
                title = "Pergunta",
                description = "Descrição da pergunta",
                subjects = mutableListOf(Subject.ARTS),
                participant = participant1,
                institution = institution1
            )
        )
        answerRepository.save(
            AnswerSchema(
                description = answerDescription,
                question = question,
                participant = participant1
            )
        )
    }

    @Test
    fun withValidValues() {
        assertDoesNotThrow {
            updateAnswerController.updateAnswer(
                id = 1L,
                UpdateAnswerController.Request(
                    description = updatedAnswerDescription,
                    participantUid = participant1Uid
                )
            )
            answerRepository.findById(1L).getOrNull()?.let {
                assert((it.id == 1L && it.description == updatedAnswerDescription))
            }
        }
    }

    @Test
    fun withBlankDescription() {
        assertThrows<ConstraintViolationException> {
            updateAnswerController.updateAnswer(
                id = 1L,
                UpdateAnswerController.Request(
                    description = " ",
                    participantUid = participant1Uid
                )
            )
        }
    }

    @Test
    fun withOverflowedDescription() {
        assertThrows<ConstraintViolationException> {
            updateAnswerController.updateAnswer(
                id = 1L,
                UpdateAnswerController.Request(
                    description = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw",
                    participantUid = participant1Uid
                )
            )
        }
    }

    @Test
    fun withInvalidId() {
        assertThrows<AnswerNotFoundException> {
            updateAnswerController.updateAnswer(
                id = 2L,
                UpdateAnswerController.Request(
                    description = updatedAnswerDescription,
                    participantUid = participant1Uid
                )
            )
        }
    }

    @Test
    fun withoutPermission() {
        assertThrows<AnswerIsNotFromParticipantException> {
            updateAnswerController.updateAnswer(
                id = 1L,
                UpdateAnswerController.Request(
                    description = updatedAnswerDescription,
                    participantUid = participant2Uid
                )
            )
        }
    }
}