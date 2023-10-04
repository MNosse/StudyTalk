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
class DeleteInstitutionApiTest @Autowired constructor(
    private val administratorRepository: AdministratorRepository,
    private val institutionRepository: InstitutionRepository
) {
    val administratorUid = "VcZSfuTj8ENjztIccfjbK2KRbHf2"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup() {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/institutions"
        administratorRepository.save(AdministratorSchema(uid = administratorUid))
        institutionRepository.save(InstitutionSchema(name = "Instituição"))
    }

    @Test
    fun withValidValues() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("administratorUid", administratorUid)
            .`when`()
            .delete("/{id}/")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun withInvalidId() {
        RestAssured.given()
            .pathParam("id", 2L)
            .param("administratorUid", administratorUid)
            .`when`()
            .delete("/{id}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("A instituição não foi encontrada."))
    }

    @Test
    fun withNonExistentAdministrator() {
        RestAssured.given()
            .pathParam("id", 1L)
            .param("administratorUid", administratorUid + "a")
            .`when`()
            .delete("/{id}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O administrador não foi encontrado."))
    }
}