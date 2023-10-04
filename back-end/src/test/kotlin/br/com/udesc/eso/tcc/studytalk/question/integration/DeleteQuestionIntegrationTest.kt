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
import br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.DeleteQuestionController
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DeleteQuestionIntegrationTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository,
    private val deleteQuestionController: DeleteQuestionController
) {
    var participantUid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        val institution = institutionRepository.save(InstitutionSchema(name = "Instituição"))
        val participant = participantRepository.save(
            ParticipantSchema(
                uid = participantUid,
                name = "Mateus Nosse",
                institution = institution
            )
        )
        questionRepository.save(
            QuestionSchema(
                title = "Pergunta",
                description = "Descrição da pergunta",
                subjects = mutableListOf(Subject.MATHEMATICS),
                participant = participant,
                institution = institution
            )
        )
    }

    @Test
    fun withValidValues() {
        assertDoesNotThrow {
            deleteQuestionController.deleteQuestionById(
                id = 1L,
                participantUid = participantUid
            )
            assert(questionRepository.findById(1L).isEmpty)
        }
    }

    @Test
    fun withInvalidId() {
        assertThrows<QuestionNotFoundException> {
            deleteQuestionController.deleteQuestionById(
                id = 2L,
                participantUid = participantUid
            )
        }
    }

    @Test
    fun withInvalidParticipant() {
        assertThrows<QuestionIsNotFromParticipantException> {
            deleteQuestionController.deleteQuestionById(
                id = 1L,
                participantUid = participantUid + "a"
            )
        }
    }
}