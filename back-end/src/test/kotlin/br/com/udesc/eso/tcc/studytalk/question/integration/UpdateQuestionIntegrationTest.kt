package br.com.udesc.eso.tcc.studytalk.question.integration

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionIsNotFromParticipantException
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.UpdateQuestionController
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
class UpdateQuestionIntegrationTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository,
    private val updateQuestionController: UpdateQuestionController
) {
    val questionTitle = "Questão"
    val questionDescription = "Descrição da Questão"
    val questionSubjects = mutableListOf(Subject.MATHEMATICS, Subject.SCIENCE)
    val updatedQuestionTitle = "Nova Questão"
    val updatedQuestionDescription = "Nova descrição da Questão"
    val updatedQuestionSubjects = mutableListOf(Subject.PHILOSOPHY, Subject.SOCIOLOGY)
    val participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    val participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"


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
                title = questionTitle,
                description = questionDescription,
                subjects = questionSubjects,
                participant = participant,
                institution = institution
            )
        )
    }

    @Test
    fun withValidValues() {
        assertDoesNotThrow {
            updateQuestionController.updateQuestion(
                id = 1L,
                UpdateQuestionController.Request(
                    title = updatedQuestionTitle,
                    description = updatedQuestionDescription,
                    subjects = updatedQuestionSubjects,
                    participantUid = participant1Uid
                )
            )
            questionRepository.findById(1L).getOrNull()?.let {
                assert(
                    it.id == 1L
                            && it.title == updatedQuestionTitle
                            && it.description == updatedQuestionDescription
                            && it.subjects.size == updatedQuestionSubjects.size
                            && it.subjects.containsAll(updatedQuestionSubjects)
                )
            }
        }
    }

    @Test
    fun withBlankValues() {
        assertDoesNotThrow {
            updateQuestionController.updateQuestion(
                id = 1L,
                UpdateQuestionController.Request(
                    title = " ",
                    description = " ",
                    subjects = null,
                    participantUid = participant1Uid
                )
            )
        }
        questionRepository.findById(1L).getOrNull()?.let {
            assert(
                it.id == 1L
                        && it.title == questionTitle
                        && it.description == questionDescription
                        && it.subjects.size == questionSubjects.size
                        && it.subjects.containsAll(questionSubjects)
            )
        }
    }

    @Test
    fun withOverflowedValues() {
        assertThrows<ConstraintViolationException> {
            updateQuestionController.updateQuestion(
                id = 1L,
                UpdateQuestionController.Request(
                    title = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw",
                    description = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw",
                    subjects = updatedQuestionSubjects,
                    participantUid = participant1Uid
                )
            )
        }
    }

    @Test
    fun withInvalidId() {
        assertThrows<QuestionNotFoundException> {
            updateQuestionController.updateQuestion(
                id = 2L,
                UpdateQuestionController.Request(
                    title = updatedQuestionTitle,
                    description = updatedQuestionDescription,
                    subjects = updatedQuestionSubjects,
                    participantUid = participant1Uid
                )
            )
        }
    }

    @Test
    fun withoutPermission() {
        assertThrows<QuestionIsNotFromParticipantException> {
            updateQuestionController.updateQuestion(
                id = 1L,
                UpdateQuestionController.Request(
                    title = updatedQuestionTitle,
                    description = updatedQuestionDescription,
                    subjects = updatedQuestionSubjects,
                    participantUid = participant1Uid + "a"
                )
            )
        }
    }
}