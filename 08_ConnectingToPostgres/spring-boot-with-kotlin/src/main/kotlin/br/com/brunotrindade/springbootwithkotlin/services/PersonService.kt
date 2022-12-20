package br.com.brunotrindade.springbootwithkotlin.services

import br.com.brunotrindade.springbootwithkotlin.exceptions.ResourceNotFoundException
import br.com.brunotrindade.springbootwithkotlin.model.Person
import br.com.brunotrindade.springbootwithkotlin.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong
import java.util.logging.Logger

@Service
class PersonService {
    @Autowired
    private lateinit var repository: PersonRepository

    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(): List<Person> {
        logger.info("Finding all people")

        return repository.findAll()
    }

    fun create(person: Person): Person {
        logger.info("Creating one person with name ${person.firstName}!")

        return repository.save(person)

    }


    fun update(person: Person): Person {
        val entity = repository.findById(person.id)
            .orElseThrow {
                ResourceNotFoundException("No records found for this id!")
            }

        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.address = person.address
        entity.gender = person.gender

        logger.info("Updating one person with id ${person.id}!")
        return repository.save(entity)
    }

    fun delete(id: Long) {
        val entity = repository.findById(id)
            .orElseThrow {
                ResourceNotFoundException("No records found for this id!")
            }

        logger.info("Deleting one person with id ${id}!")
        repository.delete(entity)
    }

    fun findById(id: Long): Person {
        logger.info("Finding a person")

        return repository.findById(id)
            .orElseThrow {
                ResourceNotFoundException("No records found for this id!")
            }
    }

}

