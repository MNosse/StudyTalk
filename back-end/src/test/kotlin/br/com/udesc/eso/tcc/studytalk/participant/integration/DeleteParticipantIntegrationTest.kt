package br.com.udesc.eso.tcc.studytalk.participant.integration

import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.DeleteParticipantController
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DeleteParticipantIntegrationTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val deleteParticipantController: DeleteParticipantController
) {
    val institutionName = "Instituição"
    val participant1Name = "Mateus Nosse"
    val participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    val participant2Name = "Mateus Coelho"
    val participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"
    var registrationCode = ""

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        val institution = institutionRepository.save(InstitutionSchema(name = institutionName))
        registrationCode = institution.registrationCode
        participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = participant1Name,
                institution = institution
            )
        )
        when (testInfo.displayName) {
            "withoutPermission()" -> {
                participantRepository.save(
                    ParticipantSchema(
                        uid = participant2Uid,
                        name = participant2Name,
                        institution = institution
                    )
                )
            }
        }
    }

    @Test
    fun withValidValues() {
        assertDoesNotThrow {
            deleteParticipantController.deleteParticipantByUid(
                participantToBeDeletedUid = participant1Uid,
                requestingParticipantUid = participant1Uid
            )
            assert(participantRepository.findById(1L).isEmpty)
        }
    }

    @Test
    fun withInvalidId() {
        assertThrows<ParticipantNotFoundException> {
            deleteParticipantController.deleteParticipantByUid(
                participantToBeDeletedUid = participant1Uid + "a",
                requestingParticipantUid = participant1Uid + "a"
            )
        }
    }

    @Test
    fun withoutPermission() {
        assertThrows<ParticipantWithoutPermissionException> {
            deleteParticipantController.deleteParticipantByUid(
                participantToBeDeletedUid = participant1Uid,
                requestingParticipantUid = participant2Uid
            )
        }
    }
}