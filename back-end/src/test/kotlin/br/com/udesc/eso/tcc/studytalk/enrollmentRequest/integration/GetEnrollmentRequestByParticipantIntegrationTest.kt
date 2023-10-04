package br.com.udesc.eso.tcc.studytalk.enrollmentRequest.integration

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.exception.EnrollmentRequestNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.enrollmentRequest.EnrollmentRequestRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.participant.ParticipantRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.enrollmentRequest.EnrollmentRequestSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GetEnrollmentRequestByParticipantIntegrationTest @Autowired constructor(
    private val institutionRepository: InstitutionRepository,
    private val enrollmentRequestRepository: EnrollmentRequestRepository,
    private val participantRepository: ParticipantRepository,
    private val getEnrollmentRequestByParticipantIdController: GetEnrollmentRequestByParticipantIdController
) {
    val participant1Uid = "VoZSfuTj8ENjztIccfjbK2KRbHf1"
    val participant1Name = "Mateus Nosse"
    var participant2Uid = "AADEiZohc1SwsBgP2qKQJKE8p8o1"
    var participant2Name = "Mateus Coelho"

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        val institution = institutionRepository.save(InstitutionSchema(name = "Instituição 1"))
        participantRepository.save(
            ParticipantSchema(
                uid = participant1Uid,
                name = participant1Name,
                privilege = when (testInfo.displayName) {
                    "withValidValues2()" -> Privilege.TEACHER
                    "withoutPrivilege()" -> Privilege.DEFAULT
                    else -> Privilege.PRINCIPAL
                },
                institution = institution
            )
        )
        val participant2 = participantRepository.save(
            ParticipantSchema(
                uid = participant2Uid,
                name = participant2Name
            )
        )
        enrollmentRequestRepository.save(
            EnrollmentRequestSchema(
                institution = institution,
                participant = participant2
            )
        )
    }

    @Test
    fun withValidValues1() {
        assertDoesNotThrow {
            val response = getEnrollmentRequestByParticipantIdController.getEnrollmentRequestByParticipantId(
                id = 2L,
                requestingParticipantUid = participant1Uid
            )
            response.participant.let {
                assert(it.uid == participant2Uid && it.name == participant2Name)
            }
        }
    }

    @Test
    fun withValidValues2() {
        assertDoesNotThrow {
            val response = getEnrollmentRequestByParticipantIdController.getEnrollmentRequestByParticipantId(
                id = 2L,
                requestingParticipantUid = participant1Uid
            )
            response.participant.let {
                assert(it.uid == participant2Uid && it.name == participant2Name)
            }
        }
    }

    @Test
    fun withoutPrivilege() {
        assertThrows<ParticipantWithoutPrivilegeException> {
            getEnrollmentRequestByParticipantIdController.getEnrollmentRequestByParticipantId(
                id = 2L,
                requestingParticipantUid = participant1Uid
            )
        }
    }

    @Test
    fun withInvalidParticipantId() {
        assertThrows<EnrollmentRequestNotFoundException> {
            getEnrollmentRequestByParticipantIdController.getEnrollmentRequestByParticipantId(
                id = 3L,
                requestingParticipantUid = participant1Uid
            )
        }
    }

    @Test
    fun withInvalidApproverUid() {
        assertThrows<ParticipantNotFoundException> {
            getEnrollmentRequestByParticipantIdController.getEnrollmentRequestByParticipantId(
                id = 2L,
                requestingParticipantUid = participant1Uid + "a"
            )
        }
    }
}