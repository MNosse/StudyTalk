package br.com.udesc.eso.tcc.studytalk.participant.integration

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
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
class DoAQuestionIntegrationTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository,
    private val doAQuestionController: DoAQuestionController
) {
    val questionTitle = "Questão"
    val questionDescription = "Descrição da Questão"
    val questionSubjects = mutableListOf(Subject.MATHEMATICS, Subject.SCIENCE)
    val participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        val institution = institutionRepository.save(InstitutionSchema(name = "Instituição 1"))
        participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = "Mateus Nosse",
                institution = institution
            )
        )
    }

    @Test
    fun withValidValues() {
        assertDoesNotThrow {
            doAQuestionController.doAQuestion(
                participantUid = participant1Uid,
                DoAQuestionController.Request(
                    title = questionTitle,
                    description = questionDescription,
                    subjects = questionSubjects
                )
            )
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
    }

    @Test
    fun withBlankValues() {
        assertThrows<ConstraintViolationException> {
            doAQuestionController.doAQuestion(
                participantUid = participant1Uid,
                DoAQuestionController.Request(
                    title = " ",
                    description = " ",
                    subjects = questionSubjects
                )
            )
        }
    }

    @Test
    fun withOverflowedDescription() {
        assertThrows<ConstraintViolationException> {
            doAQuestionController.doAQuestion(
                participantUid = participant1Uid,
                DoAQuestionController.Request(
                    title = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw",
                    description = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw",
                    subjects = questionSubjects
                )
            )
        }
    }

    @Test
    fun withNonExistentParticipant() {
        assertThrows<ParticipantNotFoundException> {
            doAQuestionController.doAQuestion(
                participantUid = participant1Uid + "a",
                DoAQuestionController.Request(
                    title = questionTitle,
                    description = questionDescription,
                    subjects = questionSubjects
                )
            )
        }
    }
}