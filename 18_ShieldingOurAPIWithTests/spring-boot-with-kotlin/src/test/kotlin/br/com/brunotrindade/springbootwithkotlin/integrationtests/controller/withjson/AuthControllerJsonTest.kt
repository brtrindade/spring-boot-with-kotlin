package br.com.brunotrindade.springbootwithkotlin.integrationtests.controller.withjson

import br.com.brunotrindade.springbootwithkotlin.integrationtests.TestConfig
import br.com.brunotrindade.springbootwithkotlin.integrationtests.testcontainer.AbstractIntegrationTest
import br.com.brunotrindade.springbootwithkotlin.integrationtests.vo.AccountCredentialsVO
import br.com.brunotrindade.springbootwithkotlin.integrationtests.vo.TokenVO
import io.restassured.RestAssured
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerJsonTest : AbstractIntegrationTest() {

    private lateinit var tokenVO: TokenVO

    @BeforeAll
    fun setupTests() {
        tokenVO = TokenVO()
    }

    @Test
    @Order(0)
    fun testLogin() {
        val user = AccountCredentialsVO(
            "admin",
            "mocked-password"
        )

        tokenVO = RestAssured.given()
            .basePath("/auth/signin")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(user)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java)

        assertNotNull(tokenVO.accessToken)
        assertNotNull(tokenVO.refreshToken)
    }


    @Test
    @Order(1)
    fun testRefresh() {

        tokenVO = RestAssured.given()
            .basePath("/auth/refresh")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParam("username", tokenVO.username)
            .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer ${tokenVO.refreshToken}")
            .`when`()
            .put("{username}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java)

        assertNotNull(tokenVO.accessToken)
        assertNotNull(tokenVO.refreshToken)
    }
}
