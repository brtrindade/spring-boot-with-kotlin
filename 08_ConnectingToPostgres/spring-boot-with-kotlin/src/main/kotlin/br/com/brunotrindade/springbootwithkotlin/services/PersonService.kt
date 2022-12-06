package br.com.brunotrindade.springbootwithkotlin.services

import br.com.brunotrindade.springbootwithkotlin.model.Person
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong
import java.util.logging.Logger

@Service
class PersonService {
    private val counter: AtomicLong = AtomicLong()
    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(): List<Person> {
        logger.info("Finding all people")

        val persons: MutableList<Person> = ArrayList()
        for (i in 0..8) {
            val person = mockPerson(i)
            persons.add(person)
        }

        return persons
    }

    fun create(person: Person) = person

    fun update(person: Person) = person

    fun delete(id: Long) {  }

    fun findById(id: Long): Person {
        logger.info("Finding a person")

        val person = Person()
        person.id = counter.incrementAndGet()
        person.firstName = "Bruno"
        person.lastName = "Trindade"
        person.address = "Rua 1"
        person.gender = "male"

        return person
    }

    private fun mockPerson(i: Int): Person {
        val person = Person()
        person.id = counter.incrementAndGet()
        person.firstName = "Person Name $i"
        person.lastName = "Last Name $i"
        person.address = "Rua $i"
        person.gender = "male"

        return person
    }
}

