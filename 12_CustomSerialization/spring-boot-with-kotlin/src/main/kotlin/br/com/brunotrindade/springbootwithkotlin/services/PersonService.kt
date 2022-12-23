package br.com.brunotrindade.springbootwithkotlin.services

import br.com.brunotrindade.springbootwithkotlin.data.vo.v1.PersonVO
import br.com.brunotrindade.springbootwithkotlin.exceptions.ResourceNotFoundException
import br.com.brunotrindade.springbootwithkotlin.mapper.DozerMapper
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

    fun findAll(): List<PersonVO> {
        logger.info("Finding all people")

        val result = repository.findAll()
        return DozerMapper.parseListObjects(result, PersonVO::class.java)
    }

    fun create(person: PersonVO): PersonVO {
        logger.info("Creating one person with name ${person.firstName}!")
        val entity = DozerMapper.parseObject(person, Person::class.java)

        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
    }


    fun update(person: PersonVO): PersonVO {
        val entity = repository.findById(person.id)
            .orElseThrow {
                ResourceNotFoundException("No records found for this id!")
            }

        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.address = person.address
        entity.gender = person.gender

        logger.info("Updating one person with id ${person.id}!")
        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
    }

    fun delete(id: Long) {
        val entity = repository.findById(id)
            .orElseThrow {
                ResourceNotFoundException("No records found for this id!")
            }

        logger.info("Deleting one person with id ${id}!")
        repository.delete(entity)
    }

    fun findById(id: Long): PersonVO {
        logger.info("Finding a person")

        val person = repository.findById(id)
            .orElseThrow {
                ResourceNotFoundException("No records found for this id!")
            }

        return DozerMapper.parseObject(person, PersonVO::class.java)
    }

}

