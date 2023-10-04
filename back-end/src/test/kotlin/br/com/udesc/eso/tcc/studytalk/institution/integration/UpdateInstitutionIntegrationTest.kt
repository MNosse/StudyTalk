package br.com.udesc.eso.tcc.studytalk.institution.integration

import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.institution.exception.InstitutionNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.administrator.AdministratorRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.administrator.AdministratorSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller.UpdateInstitutionController
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.jvm.optionals.getOrNull

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UpdateInstitutionIntegrationTest @Autowired constructor(
    private val administratorRepository: AdministratorRepository,
    private val institutionRepository: InstitutionRepository,
    private val updateInstitutionController: UpdateInstitutionController
) {
    val administratorUid = "VcZSfuTj8ENjztIccfjbK2KRbHf2"
    val institutionName = "Instituição"
    val updatedInstitutionName = "Instituição atualizada"

    @BeforeEach
    fun setup() {
        administratorRepository.save(AdministratorSchema(uid = administratorUid))
        institutionRepository.save(InstitutionSchema(name = institutionName))
    }

    @Test
    fun withValidName() {
        assertDoesNotThrow {
            updateInstitutionController.updateInstitution(
                id = 1L,
                request = UpdateInstitutionController.Request(
                    administratorUid = administratorUid,
                    name = updatedInstitutionName
                )
            )
            institutionRepository.findById(1L).getOrNull()?.let {
                assert(it.id == 1L && it.name == updatedInstitutionName && it.registrationCode.isNotBlank())
            }
        }
    }

    @Test
    fun withBlankName() {
        assertThrows<ConstraintViolationException> {
            updateInstitutionController.updateInstitution(
                id = 1L,
                request = UpdateInstitutionController.Request(
                    administratorUid = administratorUid,
                    name = " "
                )
            )
        }
    }

    @Test
    fun withOverflowedName() {
        assertThrows<ConstraintViolationException> {
            updateInstitutionController.updateInstitution(
                id = 1L,
                request = UpdateInstitutionController.Request(
                    administratorUid = administratorUid,
                    name = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw"
                )
            )
        }
    }

    @Test
    fun withInvalidId() {
        assertThrows<InstitutionNotFoundException> {
            updateInstitutionController.updateInstitution(
                id = 2L,
                request = UpdateInstitutionController.Request(
                    administratorUid = administratorUid,
                    name = updatedInstitutionName
                )
            )
        }
    }

    @Test
    fun withNonExistentAdministrator() {
        assertThrows<AdministratorNotFoundException> {
            updateInstitutionController.updateInstitution(
                id = 1L,
                request = UpdateInstitutionController.Request(
                    administratorUid = administratorUid + "a",
                    name = updatedInstitutionName
                )
            )
        }
    }
}
