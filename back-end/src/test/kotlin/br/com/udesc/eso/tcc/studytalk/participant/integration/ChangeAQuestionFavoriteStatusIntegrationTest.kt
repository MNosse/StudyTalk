package br.com.udesc.eso.tcc.studytalk.participant.integration

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerIsNotFromParticipantException
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.question.model.Question
import br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.UpdateAnswerController
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.answer.AnswerRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.question.QuestionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer.AnswerSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.ChangeAQuestionFavoriteStatusController
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
class ChangeAQuestionFavoriteStatusIntegrationTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val questionRepository: QuestionRepository,
    private val changeAQuestionFavoriteStatusController: ChangeAQuestionFavoriteStatusController
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

        questionRepository.save(
            QuestionSchema(
                title = "Pergunta",
                description = "Descrição da pergunta",
                subjects = mutableListOf(Subject.ARTS),
                participant = participant1,
                institution = institution1
            )
        )
    }

    @Test
    fun AddingToFavorite() {
        assertDoesNotThrow {
            changeAQuestionFavoriteStatusController.changeAQuestionFavoriteStatus(
                participantUid = participant2Uid,
                questionId = 1L
            )
            participantRepository.findByUid(participant2Uid)?.let {
                assert(it.id == 2L
                        && it.uid == participant2Uid
                        && containsQuestion(it, 1L)
                )
            }
        }
    }

    @Test
    fun RemovingFromFavorite() {
        assertDoesNotThrow {
            changeAQuestionFavoriteStatusController.changeAQuestionFavoriteStatus(
                participantUid = participant2Uid,
                questionId = 1L
            )
            changeAQuestionFavoriteStatusController.changeAQuestionFavoriteStatus(
                participantUid = participant2Uid,
                questionId = 1L
            )
            participantRepository.findByUid(participant2Uid)?.let {
                assert(it.id == 2L
                        && it.uid == participant2Uid
                        && !containsQuestion(it, 1L)
                )
            }
        }
    }

    @Test
    fun withNonExistentQuestion() {
        assertThrows<QuestionNotFoundException> {
            changeAQuestionFavoriteStatusController.changeAQuestionFavoriteStatus(
                participantUid = participant2Uid,
                questionId = 2L
            )
        }
    }

    @Test
    fun withNonExistentParticipant() {
        assertThrows<ParticipantNotFoundException> {
            changeAQuestionFavoriteStatusController.changeAQuestionFavoriteStatus(
                participantUid = participant2Uid + "a",
                questionId = 1L
            )
        }
    }

    private fun containsQuestion(participantSchema: ParticipantSchema, questionId: Long): Boolean {
        for (question in participantSchema.favoriteQuestions) {
            if (question.id == questionId) {
                return true
            }
        }
        return false
    }
}