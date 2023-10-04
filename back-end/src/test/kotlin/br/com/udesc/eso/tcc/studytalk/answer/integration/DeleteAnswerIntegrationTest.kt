package br.com.udesc.eso.tcc.studytalk.answer.integration

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerIsNotFromParticipantException
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.DeleteAnswerController
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
class DeleteAnswerIntegrationTest @Autowired constructor(
    private val answerRepository: AnswerRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository,
    private val deleteAnswerController: DeleteAnswerController
) {
    var participantUid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    var answerDescription = "Resposta 1"

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        val institution = institutionRepository.save(InstitutionSchema(name = "Instituição 1"))
        val participant = participantRepository.save(
            ParticipantSchema(
                uid = participantUid,
                name = "Mateus Nosse",
                institution = institution
            )
        )
        val question = questionRepository.save(
            QuestionSchema(
                title = "Pergunta 1",
                description = "Descrição da pergunta 1",
                subjects = mutableListOf(Subject.ARTS),
                participant = participant,
                institution = institution
            )
        )
        answerRepository.save(
            AnswerSchema(
                description = answerDescription,
                question = question,
                participant = participant
            )
        )
    }

    @Test
    fun withValidValues() {
        assertDoesNotThrow {
            deleteAnswerController.deleteAnswerById(
                id = 1L,
                participantUid = participantUid
            )
            assert(answerRepository.findById(1L).isEmpty)
        }
    }

    @Test
    fun withInvalidId() {
        assertThrows<AnswerNotFoundException> {
            deleteAnswerController.deleteAnswerById(
                id = 2L,
                participantUid = participantUid
            )
        }
    }

    @Test
    fun withInvalidParticipant() {
        assertThrows<AnswerIsNotFromParticipantException> {
            deleteAnswerController.deleteAnswerById(
                id = 1L,
                participantUid = participantUid + "a"
            )
        }
    }
}