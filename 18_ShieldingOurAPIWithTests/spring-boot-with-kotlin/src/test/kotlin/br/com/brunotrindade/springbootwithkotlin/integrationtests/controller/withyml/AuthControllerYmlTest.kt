package br.com.brunotrindade.springbootwithkotlin.integrationtests.controller.withyml

import br.com.brunotrindade.springbootwithkotlin.integrationtests.TestConfig
import br.com.brunotrindade.springbootwithkotlin.integrationtests.controller.withyml.mapper.YmlMapper
import br.com.brunotrindade.springbootwithkotlin.integrationtests.testcontainer.AbstractIntegrationTest
import br.com.brunotrindade.springbootwithkotlin.integrationtests.vo.AccountCredentialsVO
import br.com.brunotrindade.springbootwithkotlin.integrationtests.vo.TokenVO
import io.restassured.RestAssured
import io.restassured.config.EncoderConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.ContentType
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerYmlTest : AbstractIntegrationTest() {

    private lateinit var objectMapper: YmlMapper
    private lateinit var tokenVO: TokenVO

    @BeforeAll
    fun setupTests() {
        objectMapper = YmlMapper()
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
            .config(
                RestAssuredConfig.config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfig.CONTENT_TYPE_YAML, ContentType.TEXT)
                    )
            )
            .basePath("/auth/signin")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_YAML)
            .accept(TestConfig.CONTENT_TYPE_YAML)
            .body(user, objectMapper)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java, objectMapper)

        assertNotNull(tokenVO.accessToken)
        assertNotNull(tokenVO.refreshToken)
    }


    @Test
    @Order(1)
    fun testRefresh() {

        tokenVO = RestAssured.given()
            .config(
                RestAssuredConfig.config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfig.CONTENT_TYPE_YAML, ContentType.TEXT)
                    )
            )
            .basePath("/auth/refresh")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_YAML)
            .accept(TestConfig.CONTENT_TYPE_YAML)
            .pathParam("username", tokenVO.username)
            .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer ${tokenVO.refreshToken}")
            .`when`()
            .put("{username}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java, objectMapper)

        assertNotNull(tokenVO.accessToken)
        assertNotNull(tokenVO.refreshToken)
    }
}
