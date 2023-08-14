package br.com.brunotrindade.springbootwithkotlin.integrationtests.controller.cors.withjson

import br.com.brunotrindade.springbootwithkotlin.integrationtests.TestConfig
import br.com.brunotrindade.springbootwithkotlin.integrationtests.testcontainer.AbstractIntegrationTest
import br.com.brunotrindade.springbootwithkotlin.integrationtests.vo.AccountCredentialsVO
import br.com.brunotrindade.springbootwithkotlin.integrationtests.vo.PersonVO
import br.com.brunotrindade.springbootwithkotlin.integrationtests.vo.TokenVO
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerCorsWithJson : AbstractIntegrationTest() {

    private lateinit var specification: RequestSpecification
    private lateinit var objectMapper: ObjectMapper
    private lateinit var person: PersonVO

    private lateinit var token: String

    @BeforeAll
    fun setupTests() {
        objectMapper = ObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

        person = PersonVO()
        token = ""
    }

    @Test
    @Order(0)
    fun authorization() {
        val user = AccountCredentialsVO(
            "admin",
            "mocked-password"
        )

        token = given()
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
            .accessToken!!
    }

    @Test
    @Order(1)
    fun testCreate() {
        mockPerson()

        specification = RequestSpecBuilder()
            .addHeader(
                TestConfig.HEADER_PARAM_ORIGIN,
                TestConfig.ORIGIN_BRUNO
            )
            .addHeader(
                TestConfig.HEADER_PARAM_AUTHORIZATION,
                "Bearer $token"
            )
            .setBasePath("api/v1/person")
            .setPort(TestConfig.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()

        val content = given()
            .spec(specification)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(person)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val createdPerson = objectMapper.readValue(content, PersonVO::class.java)
        person = createdPerson

        assertNotNull(createdPerson.id)
        assertNotNull(createdPerson.firstName)
        assertNotNull(createdPerson.lastName)
        assertNotNull(createdPerson.address)
        assertNotNull(createdPerson.gender)
        assertTrue(createdPerson.id > 0)

        assertEquals("Fulano", createdPerson.firstName)
        assertEquals("de Tal", createdPerson.lastName)
        assertEquals("Some place", createdPerson.address)
        assertEquals("Male", createdPerson.gender)
    }

    @Test
    @Order(2)
    fun testFindById() {
        mockPerson()

        specification = RequestSpecBuilder()
            .addHeader(
                TestConfig.HEADER_PARAM_ORIGIN,
                TestConfig.ORIGIN_LOCALHOST
            )
            .addHeader(
                TestConfig.HEADER_PARAM_AUTHORIZATION,
                "Bearer $token"
            )
            .setBasePath("api/v1/person")
            .setPort(TestConfig.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()

        val content = given()
            .spec(specification)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParam("id", person.id)
            .`when`()["{id}"]
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()


        val createdPerson = objectMapper.readValue(content, PersonVO::class.java)
        assertNotNull(createdPerson.id)
        assertNotNull(createdPerson.firstName)
        assertNotNull(createdPerson.lastName)
        assertNotNull(createdPerson.address)
        assertNotNull(createdPerson.gender)
        assertTrue(createdPerson.id > 0)

        assertEquals("Fulano", createdPerson.firstName)
        assertEquals("de Tal", createdPerson.lastName)
        assertEquals("Some place", createdPerson.address)
        assertEquals("Male", createdPerson.gender)
    }

    @Test
    @Order(3)
    fun testCreateWithWrongOrigin() {
        mockPerson()

        specification = RequestSpecBuilder()
            .addHeader(
                TestConfig.HEADER_PARAM_ORIGIN,
                TestConfig.ORIGIN_GOOGLE
            )
            .addHeader(
                TestConfig.HEADER_PARAM_AUTHORIZATION,
                "Bearer $token"
            )
            .setBasePath("api/v1/person")
            .setPort(TestConfig.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()

        val content = given()
            .spec(specification)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(person)
            .`when`()
            .post()
            .then()
            .statusCode(403)
            .extract()
            .body()
            .asString()

        assertEquals("Invalid CORS request", content)
    }

    @Test
    @Order(4)
    fun testFindByIdWithWrongOrigin() {
        mockPerson()

        specification = RequestSpecBuilder()
            .addHeader(
                TestConfig.HEADER_PARAM_ORIGIN,
                TestConfig.ORIGIN_GOOGLE
            )
            .addHeader(
                TestConfig.HEADER_PARAM_AUTHORIZATION,
                "Bearer $token"
            )
            .setBasePath("api/v1/person")
            .setPort(TestConfig.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()

        val content = given()
            .spec(specification)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParam("id", person.id)
            .`when`()["{id}"]
            .then()
            .statusCode(403)
            .extract()
            .body()
            .asString()

        assertEquals("Invalid CORS request", content)
    }

    private fun mockPerson() {
        person.firstName = "Fulano"
        person.lastName = "de Tal"
        person.address = "Some place"
        person.gender = "Male"
    }

}