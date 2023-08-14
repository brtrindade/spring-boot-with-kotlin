package br.com.brunotrindade.mockk.services

import br.com.brunotrindade.springbootwithkotlin.exceptions.RequiredObjectIsNullException
import br.com.brunotrindade.springbootwithkotlin.repository.PersonRepository
import br.com.brunotrindade.springbootwithkotlin.services.PersonService
import br.com.brunotrindade.springbootwithkotlin.unittests.mapper.mocks.MockPerson
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class PersonServiceTest {

    private lateinit var inputObject: MockPerson

    @InjectMockKs
    private lateinit var service: PersonService

    @MockK
    private lateinit var repository: PersonRepository

    @BeforeEach
    fun setUpMock() {
        inputObject = MockPerson()
        MockKAnnotations.init(this)
    }

    @Test
    fun findAll() {
        val list = inputObject.mockEntityList()
        every { repository.findAll() } returns list

        val persons = service.findAll()

        assertNotNull(persons)
        assertEquals(14, persons.size)

        val personOne = persons[1]

        assertNotNull(personOne)
        assertNotNull(personOne.key)
        assertNotNull(personOne.links)
        assertTrue(personOne.links.toString().contains("</api/v1/person/1>;rel=\"self\""))
        assertEquals("Address Test1", personOne.address)
        assertEquals("First Name Test1", personOne.firstName)
        assertEquals("Last Name Test1", personOne.lastName)
        assertEquals("Female", personOne.gender)

        val personFour = persons[4]

        assertNotNull(personFour)
        assertNotNull(personFour.key)
        assertNotNull(personFour.links)
        assertTrue(personFour.links.toString().contains("</api/v1/person/4>;rel=\"self\""))
        assertEquals("Address Test4", personFour.address)
        assertEquals("First Name Test4", personFour.firstName)
        assertEquals("Last Name Test4", personFour.lastName)
        assertEquals("Male", personFour.gender)

        val personSeven = persons[7]

        assertNotNull(personSeven)
        assertNotNull(personSeven.key)
        assertNotNull(personSeven.links)
        assertTrue(personSeven.links.toString().contains("</api/v1/person/7>;rel=\"self\""))
        assertEquals("Address Test7", personSeven.address)
        assertEquals("First Name Test7", personSeven.firstName)
        assertEquals("Last Name Test7", personSeven.lastName)
        assertEquals("Female", personSeven.gender)
    }

    @Test
    fun createWithNullPerson() {
        val exception: Exception = assertThrows(RequiredObjectIsNullException::class.java) {
            service.create(null)
        }

        val expectedMessage = "It is not allowed to persist a null object!"
        val actualMessage = exception.message

        assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun updateWithNullPerson() {
        val exception: Exception = assertThrows(RequiredObjectIsNullException::class.java) {
            service.update(null)
        }

        val expectedMessage = "It is not allowed to persist a null object!"
        val actualMessage = exception.message

        assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun create() {
        val entity = inputObject.mockEntity(1)

        val persisted = entity.copy()
        persisted.id = 1

        every { repository.save(entity) } returns persisted

        val vo = inputObject.mockVO(1)
        val result = service.create(vo)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("</api/v1/person/1>;rel=\"self\""))
        assertEquals("Address Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    fun update() {
        val entity = inputObject.mockEntity(1)

        val persisted = entity.copy()
        persisted.id = 1

        every { repository.findById(1) } returns Optional.of(entity)
        every { repository.save(entity) } returns persisted

        val vo = inputObject.mockVO(1)
        val result = service.update(vo)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("</api/v1/person/1>;rel=\"self\""))
        assertEquals("Address Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    fun delete() {
        val id = 1L
        val entity = inputObject.mockEntity(id.toInt())

        every { repository.findById(id) } returns Optional.of(entity)
        every { repository.delete(any()) } just runs
        service.delete(id)
    }

    @Test
    fun findById() {
        val person = inputObject.mockEntity(1)
        person.id = 1
        every { repository.findById(1) } returns Optional.of(person)

        val result = service.findById(1)


        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("</api/v1/person/1>;rel=\"self\""))
        assertEquals("Address Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }
}