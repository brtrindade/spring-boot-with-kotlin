package br.com.brunotrindade.mockk.services

import br.com.brunotrindade.springbootwithkotlin.exceptions.RequiredObjectIsNullException
import br.com.brunotrindade.springbootwithkotlin.repository.BookRepository
import br.com.brunotrindade.springbootwithkotlin.services.BookService
import br.com.brunotrindade.springbootwithkotlin.unittests.mapper.mocks.MockBook
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
internal class BookServiceTest {

    private lateinit var inputObject: MockBook

    @InjectMockKs
    private lateinit var service: BookService

    @MockK
    private lateinit var repository: BookRepository

    @BeforeEach
    fun setUpMock() {
        inputObject = MockBook()
        MockKAnnotations.init(this)
    }

    @Test
    fun findAll() {
        val list = inputObject.mockEntityList()
        every { repository.findAll() } returns list
        println("List total: ${list.size}")

        val books = service.findAll()
        println("Books total: ${books.size}")

        assertNotNull(books)
        assertEquals(14, books.size)

        val bookOne = books[1]

        assertNotNull(bookOne)
        assertNotNull(bookOne.key)
        assertNotNull(bookOne.links)
        assertTrue(bookOne.links.toString().contains("</api/v1/book/1>;rel=\"self\""))
        assertEquals("Some Title1", bookOne.title)
        assertEquals("Some Author1", bookOne.author)
        assertEquals(25.0, bookOne.price)

        val bookFour = books[4]

        assertNotNull(bookFour)
        assertNotNull(bookFour.key)
        assertNotNull(bookFour.links)
        assertTrue(bookFour.links.toString().contains("</api/v1/book/4>;rel=\"self\""))
        assertEquals("Some Title4", bookFour.title)
        assertEquals("Some Author4", bookFour.author)
        assertEquals(25.0, bookFour.price)

        val bookSeven = books[7]

        assertNotNull(bookSeven)
        assertNotNull(bookSeven.key)
        assertNotNull(bookSeven.links)
        assertTrue(bookSeven.links.toString().contains("</api/v1/book/7>;rel=\"self\""))
        assertEquals("Some Title7", bookSeven.title)
        assertEquals("Some Author7", bookSeven.author)
        assertEquals(25.0, bookSeven.price)
    }

    @Test
    fun findById() {
        val book = inputObject.mockEntity(1)
        book.id = 1
        every { repository.findById(1) } returns Optional.of(book)

        val result = service.findById(1)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("</api/v1/book/1>;rel=\"self\""))
        assertEquals("Some Title1", result.title)
        assertEquals("Some Author1", result.author)
        assertEquals(25.0, result.price)
    }

    @Test
    fun create() {
        val entity = inputObject.mockEntity(1)

        val persisted = entity.copy()
        persisted.id = 1

        every { repository.save(entity) } returns persisted

        val vo = inputObject.mockVO(1)
        val result = service.create(vo)
        println(result)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("</api/v1/book/1>;rel=\"self\""))
        assertEquals("Some Title1", result.title)
        assertEquals("Some Author1", result.author)
        assertEquals(25.0, result.price)
    }

    @Test
    fun createWithNullBook() {
        val exception: Exception = assertThrows(
            RequiredObjectIsNullException::class.java
        ) { service.create(null) }

        val expectedMessage = "It is not allowed to persist a null object!"
        val actualMessage = exception.message
        assertTrue(actualMessage!!.contains(expectedMessage))
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
        assertTrue(result.links.toString().contains("</api/v1/book/1>;rel=\"self\""))
        assertEquals("Some Title1", result.title)
        assertEquals("Some Author1", result.author)
        assertEquals(25.0, result.price)
    }

    @Test
    fun updateWithNullBook() {
        val exception: Exception = assertThrows(
            RequiredObjectIsNullException::class.java
        ) { service.update(null) }

        val expectedMessage = "It is not allowed to persist a null object!"
        val actualMessage = exception.message
        assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun delete() {
        val id = 1L
        val entity = inputObject.mockEntity(id.toInt())
        every { repository.findById(id) } returns Optional.of(entity)
        every { repository.delete(any()) } just runs
        service.delete(id)
    }
}