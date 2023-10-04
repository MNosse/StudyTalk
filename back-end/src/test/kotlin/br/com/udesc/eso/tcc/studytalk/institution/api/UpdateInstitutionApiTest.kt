package br.com.udesc.eso.tcc.studytalk.institution.api

import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.administrator.AdministratorRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.institution.InstitutionRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.administrator.AdministratorSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import io.restassured.RestAssured
import io.restassured.http.ContentType
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
class UpdateInstitutionApiTest @Autowired constructor(
    private val administratorRepository: AdministratorRepository,
    private val institutionRepository: InstitutionRepository
) {
    val administratorUid = "VcZSfuTj8ENjztIccfjbK2KRbHf2"
    val institutionName = "Instituição"
    val updatedInstitutionName = "Instituição atualizada"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup() {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/institutions"
        administratorRepository.save(AdministratorSchema(uid = administratorUid))
        institutionRepository.save(InstitutionSchema(name = institutionName))
    }

    @Test
    fun withValidName() {
        val requestBody = mapOf(
            "administratorUid" to administratorUid,
            "name" to updatedInstitutionName
        )
        RestAssured.given()
            .pathParam("id", 1L)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{id}/")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun withBlankName() {
        val requestBody = mapOf(
            "administratorUid" to administratorUid,
            "name" to " "
        )
        RestAssured.given()
            .pathParam("id", 1L)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{id}/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("name", CoreMatchers.equalTo("O nome deve conter ao menos um caractere."))
    }

    @Test
    fun withOverflowedName() {
        val requestBody = mapOf(
            "administratorUid" to administratorUid,
            "name" to "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw"
        )
        RestAssured.given()
            .pathParam("id", 1L)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{id}/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("name", CoreMatchers.equalTo("O nome não pode ultrapassar 256 caracteres."))
    }

    @Test
    fun withInvalidId() {
        val requestBody = mapOf(
            "administratorUid" to administratorUid,
            "name" to updatedInstitutionName
        )
        RestAssured.given()
            .pathParam("id", 3L)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{id}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("A instituição não foi encontrada."))
    }

    @Test
    fun withNonExistentAdministrator() {
        val requestBody = mapOf(
            "administratorUid" to administratorUid + "a",
            "name" to updatedInstitutionName
        )
        RestAssured.given()
            .pathParam("id", 1L)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .put("/{id}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O administrador não foi encontrado."))
    }
}
