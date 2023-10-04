package br.com.udesc.eso.tcc.studytalk.participant.integration

import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.UpdateParticipantController
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
class UpdateParticipantIntegrationTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val updateParticipantController: UpdateParticipantController
) {
    val institutionName = "Instituição"
    val participantName = "Mateus Nosse"
    val participantUid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    val updateParticipantName = "Mateus Coelho Nosse"

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        val institution = institutionRepository.save(InstitutionSchema(name = institutionName))
        participantRepository.save(
            ParticipantSchema(
                uid = participantUid,
                name = participantName,
                institution = institution
            )
        )
    }

    @Test
    fun withValidName() {
        assertDoesNotThrow {
            updateParticipantController.updateParticipant(
                participantToBeUpdatedUid = participantUid,
                UpdateParticipantController.Request(
                    requestingParticipantUid = participantUid,
                    name = updateParticipantName
                )
            )
            participantRepository.findById(1L).getOrNull()?.let {
                assert(
                    it.id == 1L
                            && it.uid == participantUid
                            && it.name == updateParticipantName
                )
            }
        }
    }

    @Test
    fun withBlankName() {
        assertThrows<ConstraintViolationException> {
            updateParticipantController.updateParticipant(
                participantToBeUpdatedUid = participantUid,
                UpdateParticipantController.Request(
                    requestingParticipantUid = participantUid,
                    name = " "
                )
            )
        }
    }

    @Test
    fun withOverflowedName() {
        assertThrows<ConstraintViolationException> {
            updateParticipantController.updateParticipant(
                participantToBeUpdatedUid = participantUid,
                UpdateParticipantController.Request(
                    requestingParticipantUid = participantUid,
                    name = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw"
                )
            )
        }
    }

    @Test
    fun withNonExixtentParticipant() {
        assertThrows<ParticipantNotFoundException> {
            updateParticipantController.updateParticipant(
                participantToBeUpdatedUid = participantUid + "a",
                UpdateParticipantController.Request(
                    requestingParticipantUid = participantUid + "a",
                    name = updateParticipantName
                )
            )
        }
    }

    @Test
    fun withoutPermission() {
        assertThrows<ParticipantWithoutPermissionException> {
            updateParticipantController.updateParticipant(
                participantToBeUpdatedUid = participantUid + "a",
                UpdateParticipantController.Request(
                    requestingParticipantUid = participantUid,
                    name = updateParticipantName
                )
            )
        }
    }
}