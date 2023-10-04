package br.com.udesc.eso.tcc.studytalk.administrator.api

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CreateAdministratorApiTest {
    val administratorUid = "VcZSfuTj8ENjztIccfjbK2KRbHf2"

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.baseURI = "http://localhost:$port/studytalk/api/administrators"
    }

    @Test
    fun withValidUid() {
        val requestBody = mapOf(
            "uid" to administratorUid
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
    fun withInvalidUid() {
        val requestBody = mapOf(
            "uid" to " "
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("/")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .contentType(ContentType.JSON)
            .body("uid", equalTo("O UID deve conter ao menos um caractere."))
    }
}
