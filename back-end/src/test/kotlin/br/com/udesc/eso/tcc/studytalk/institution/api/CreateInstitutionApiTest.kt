package br.com.udesc.eso.tcc.studytalk.institution.api

import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.administrator.AdministratorRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.administrator.AdministratorSchema
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
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
class CreateInstitutionApiTest @Autowired constructor(private val administratorRepository: AdministratorRepository) {
    val administratorUid = "VcZSfuTj8ENjztIccfjbK2KRbHf2"
    val institutionName = "Instituição"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/institutions"
        administratorRepository.save(AdministratorSchema(uid = administratorUid))
    }

    @Test
    fun withValidName() {
        val requestBody = mapOf(
            "administratorUid" to administratorUid,
            "name" to institutionName
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/")
            .then()
            .statusCode(HttpStatus.CREATED.value())
    }

    @Test
    fun withBlankName() {
        val requestBody = mapOf(
            "administratorUid" to administratorUid,
            "name" to " "
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .contentType(ContentType.JSON)
            .body("name", equalTo("O nome deve conter ao menos um caractere."))
    }

    @Test
    fun withOverflowedName() {
        val requestBody = mapOf(
            "administratorUid" to administratorUid,
            "name" to "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw"
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .contentType(ContentType.JSON)
            .body("name", equalTo("O nome não pode ultrapassar 256 caracteres."))
    }

    @Test
    fun withNonExistentAdministrator() {
        val requestBody = mapOf(
            "administratorUid" to administratorUid + "a",
            "name" to institutionName
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", equalTo("O administrador não foi encontrado."))

    }
}
