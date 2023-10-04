package br.com.udesc.eso.tcc.studytalk.participant.integration

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerIsNotFromParticipantException
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.UpdateAnswerController
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.answer.AnswerRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer.AnswerSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.ChangeAnAnswerLikeStatusController
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
class ChangeAnAnswerLikeStatusIntegrationTest @Autowired constructor(
    private val answerRepository: AnswerRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository,
    private val changeAnAnswerLikeStatusController: ChangeAnAnswerLikeStatusController
) {
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
                description = "Descrição da Resposta",
                question = question,
                participant = participant1
            )
        )
    }

    @Test
    fun IncreasingLikeCount() {
        assertDoesNotThrow {
            changeAnAnswerLikeStatusController.changeAnAnswerLikeStatus(
                participantUid = participant2Uid,
                answerId = 1L
            )
            answerRepository.findById(1L).getOrNull()?.let {
                assert((it.id == 1L && it.likeCount == 1))
            }
        }
    }

    @Test
    fun DecreasingLikeCount() {
        assertDoesNotThrow {
            changeAnAnswerLikeStatusController.changeAnAnswerLikeStatus(
                participantUid = participant2Uid,
                answerId = 1L
            )
            changeAnAnswerLikeStatusController.changeAnAnswerLikeStatus(
                participantUid = participant2Uid,
                answerId = 1L
            )
            answerRepository.findById(1L).getOrNull()?.let {
                assert((it.id == 1L && it.likeCount == 0))
            }
        }
    }

    @Test
    fun withNonExistentAnswer() {
        assertThrows<AnswerNotFoundException> {
            changeAnAnswerLikeStatusController.changeAnAnswerLikeStatus(
                participantUid = participant2Uid,
                answerId = 2L
            )
        }
    }

    @Test
    fun withNonExistentParticipant() {
        assertThrows<ParticipantNotFoundException> {
            changeAnAnswerLikeStatusController.changeAnAnswerLikeStatus(
                participantUid = participant2Uid + "a",
                answerId = 1L
            )
        }
    }
}