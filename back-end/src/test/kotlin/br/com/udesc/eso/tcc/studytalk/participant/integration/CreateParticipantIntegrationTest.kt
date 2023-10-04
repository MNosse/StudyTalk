package br.com.udesc.eso.tcc.studytalk.participant.integration

import br.com.udesc.eso.tcc.studytalk.entity.institution.exception.InstitutionNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.enrollmentRequest.EnrollmentRequestRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.CreateParticipantController
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
class CreateParticipantIntegrationTest @Autowired constructor(
    private val enrollmentRequestRepository: EnrollmentRequestRepository,
    private val institutionRepository: InstitutionRepository,
    private val participantRepository: ParticipantRepository,
    private val createParticipantController: CreateParticipantController
) {
    val institutionName = "Instituição"
    var registrationCode = ""
    val participantName = "Mateus Nosse"
    val participantUid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        val institution = institutionRepository.save(InstitutionSchema(name = institutionName))
        registrationCode = institution.registrationCode
    }

    @Test
    fun withValidName() {
        assertDoesNotThrow {
            createParticipantController.createParticipant(
                CreateParticipantController.Request(
                    registrationCode = registrationCode,
                    uid = participantUid,
                    name = participantName
                )
            )
            participantRepository.findById(1L).getOrNull()?.let {
                assert(
                    it.id == 1L
                            && it.uid == participantUid
                            && it.name == participantName
                )
            }
            enrollmentRequestRepository.findById(1L).getOrNull()?.let {
                assert(
                    it.institution.id == 1L
                            && it.institution.registrationCode == registrationCode
                            && it.institution.name == institutionName
                            && it.participant.id == 1L
                            && it.participant.uid == participantUid
                            && it.participant.name == participantName
                )
            }
        }
    }

    @Test
    fun withBlankName() {
        assertThrows<ConstraintViolationException> {
            createParticipantController.createParticipant(
                CreateParticipantController.Request(
                    registrationCode = registrationCode,
                    uid = participantUid,
                    name = " "
                )
            )
        }
    }

    @Test
    fun withOverflowedName() {
        assertThrows<ConstraintViolationException> {
            createParticipantController.createParticipant(
                CreateParticipantController.Request(
                    registrationCode = registrationCode,
                    uid = participantUid,
                    name = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw"
                )
            )
        }
    }

    @Test
    fun withNonExistentInstitution() {
        assertThrows<InstitutionNotFoundException> {
            createParticipantController.createParticipant(
                CreateParticipantController.Request(
                    registrationCode = registrationCode + "a",
                    uid = participantUid,
                    name = participantName
                )
            )
        }
    }
}