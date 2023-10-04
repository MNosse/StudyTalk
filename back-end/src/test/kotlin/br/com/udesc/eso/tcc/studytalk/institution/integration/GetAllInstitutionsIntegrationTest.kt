package br.com.udesc.eso.tcc.studytalk.institution.integration

import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.administrator.AdministratorRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.administrator.AdministratorSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller.GetAllInstitutionsController
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
class GetAllInstitutionsIntegrationTest @Autowired constructor(
    private val administratorRepository: AdministratorRepository,
    private val institutionRepository: InstitutionRepository,
    private val getAllInstitutionsController: GetAllInstitutionsController
) {
    val administratorUid = "VcZSfuTj8ENjztIccfjbK2KRbHf2"
    val institution1Name = "Instituição 1"
    val institution2Name = "Instituição 2"

    @BeforeEach
    fun setup() {
        administratorRepository.save(AdministratorSchema(uid = administratorUid))
        institutionRepository.save(InstitutionSchema(name = institution1Name))
        institutionRepository.save((InstitutionSchema(name = institution2Name)))
    }

    @Test
    fun withValidValues() {
        assertDoesNotThrow {
            val response = getAllInstitutionsController.getAllInstitutions(
                administratorUid = administratorUid
            )
            response.institutions.let {
                for (institution in it) {
                    assert(
                        (institution.id == 1L && institution.name == institution1Name)
                                || (institution.id == 2L && institution.name == institution2Name)
                                && institution.registrationCode.isNotBlank()
                    )
                }
            }
        }
    }

    @Test
    fun withNonExistentAdministrator() {
        assertThrows<AdministratorNotFoundException> {
            getAllInstitutionsController.getAllInstitutions(
                administratorUid = administratorUid + "a"
            )
        }
    }
}