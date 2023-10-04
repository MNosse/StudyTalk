package br.com.udesc.eso.tcc.studytalk.administrator.api

import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.repository.administrator.AdministratorRepository
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.administrator.AdministratorSchema
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
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
class GetAdministratorApiTest @Autowired constructor(private val administratorRepository: AdministratorRepository) {
    val administratorUid = "VcZSfuTj8ENjztIccfjbK2KRbHf2"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/administrators"
        administratorRepository.save(AdministratorSchema(uid = administratorUid))
    }

    @Test
    fun withValidUid() {
        RestAssured.given()
            .pathParam("uid", administratorUid)
            .`when`()
            .get("/{uid}/")
            .then()
            .statusCode(HttpStatus.FOUND.value())
            .contentType(ContentType.JSON)
            .body("uid", equalTo(administratorUid))
    }

    @Test
    fun withInvalidUid() {
        RestAssured.given()
            .pathParam("uid", administratorUid + "a")
            .`when`()
            .get("/{uid}/")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("error", CoreMatchers.equalTo("O administrador não foi encontrado."))
    }
}
