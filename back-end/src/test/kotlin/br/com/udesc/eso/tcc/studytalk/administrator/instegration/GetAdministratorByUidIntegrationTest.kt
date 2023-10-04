package br.com.udesc.eso.tcc.studytalk.administrator.instegration

import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.administrator.controller.GetAdministratorByUidController
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.administrator.AdministratorRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.administrator.AdministratorSchema
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GetAdministratorByUidIntegrationTest @Autowired constructor(
    private val administratorRepository: AdministratorRepository,
    private val getAdministratorByUidController: GetAdministratorByUidController
) {
    var administratorUid = "VcZSfuTj8ENjztIccfjbK2KRbHf2"

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        administratorRepository.save(AdministratorSchema(uid = administratorUid))
    }

    @Test
    fun withValidUid() {
        assertDoesNotThrow {
            getAdministratorByUidController.getAdministratorByUid(administratorUid)
            administratorRepository.findByUid(administratorUid)?.let {
                assert(it.id == 1L && it.uid == administratorUid)
            }
        }
    }

    @Test
    fun withInvalidUid() {
        assertThrows<AdministratorNotFoundException> {
            getAdministratorByUidController.getAdministratorByUid(administratorUid + "a")
        }
    }
}
