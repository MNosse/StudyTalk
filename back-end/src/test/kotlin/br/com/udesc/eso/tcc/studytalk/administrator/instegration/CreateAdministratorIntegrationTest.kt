package br.com.udesc.eso.tcc.studytalk.administrator.instegration

import br.com.udesc.eso.tcc.studytalk.infrastructure.administrator.controller.CreateAdministratorController
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.administrator.AdministratorRepository
import jakarta.validation.ConstraintViolationException
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
class CreateAdministratorIntegrationTest @Autowired constructor(
    private val administratorRepository: AdministratorRepository,
    private val createAdministratorController: CreateAdministratorController
) {
    var administratorUid = "VcZSfuTj8ENjztIccfjbK2KRbHf2"

    @Test
    fun withValidUid() {
        assertDoesNotThrow {
            createAdministratorController.createAdministrator(CreateAdministratorController.Request(administratorUid))
            administratorRepository.findByUid(administratorUid)?.let {
                assert(it.id == 1L && it.uid == administratorUid)
            }
        }
    }

    @Test
    fun withInvalidUid() {
        assertThrows<ConstraintViolationException> {
            createAdministratorController.createAdministrator(CreateAdministratorController.Request(" "))
        }
    }
}
