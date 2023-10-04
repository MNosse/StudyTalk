package br.com.udesc.eso.tcc.studytalk.institution.api

import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.administrator.AdministratorRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.administrator.AdministratorSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GetAllInstitutionsIntegrationTest @Autowired constructor(
    private val administratorRepository: AdministratorRepository,
    private val institutionRepository: InstitutionRepository
) {
    val administratorUid = "VcZSfuTj8ENjztIccfjbK2KRbHf2"
    val institution1Name = "Instituição 1"
    var institution1RegistrationCode = ""
    val institution2Name = "Instituição 2"
    var institution2RegistrationCode = ""

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup() {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/institutions"
        administratorRepository.save(AdministratorSchema(uid = administratorUid))
        val institution1 = institutionRepository.save(InstitutionSchema(name = institution1Name))
        institution1RegistrationCode = institution1.registrationCode
        val institution2 = institutionRepository.save((InstitutionSchema(name = institution2Name)))
        institution2RegistrationCode = institution2.registrationCode
    }

    @Test
    fun withValidValues() {
        val expectedResponse = listOf(
            mapOf("id" to 1, "registrationCode" to institution1RegistrationCode, "name" to "Instituição 1"),
            mapOf("id" to 2, "registrationCode" to institution2RegistrationCode, "name" to "Instituição 2")
        )

        RestAssured.given()
            .param("administratorUid", administratorUid)
            .`when`()
            .get("/")
            .then()
            .statusCode(HttpStatus.FOUND.value())
            .body("institutions", CoreMatchers.equalTo(expectedResponse))
    }

    @Test
    fun withNonExistentAdministrator() {
        RestAssured.given()
            .param("administratorUid", administratorUid + "a")
            .`when`()
            .get("/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O administrador não foi encontrado."))
    }
}