package br.com.udesc.eso.tcc.studytalk.institution.integration

import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.institution.exception.InstitutionNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.administrator.AdministratorRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.administrator.AdministratorSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller.DeleteInstitutionController
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DeleteInstitutionIntegrationTest @Autowired constructor(
    private val administratorRepository: AdministratorRepository,
    private val institutionRepository: InstitutionRepository,
    private val deleteInstitutionController: DeleteInstitutionController
) {
    var administratorUid = "VcZSfuTj8ENjztIccfjbK2KRbHf2"

    @BeforeEach
    fun setup() {
        administratorRepository.save(AdministratorSchema(uid = administratorUid))
        institutionRepository.save(InstitutionSchema(name = "Instituição"))
    }

    @Test
    fun withValidValues() {
        assertDoesNotThrow {
            deleteInstitutionController.deleteInstitutionById(
                id = 1L,
                administratorUid = administratorUid
            )
            assert(institutionRepository.findById(1L).isEmpty)
        }
    }

    @Test
    fun withInvalidId() {
        assertThrows<InstitutionNotFoundException> {
            deleteInstitutionController.deleteInstitutionById(
                id = 2L,
                administratorUid = administratorUid
            )
        }
    }

    @Test
    fun withNonExistentAdministrator() {
        assertThrows<AdministratorNotFoundException> {
            deleteInstitutionController.deleteInstitutionById(
                id = 1L,
                administratorUid = administratorUid + "a"
            )
        }
    }
}