package br.com.brunotrindade.springbootwithkotlin.services

import br.com.brunotrindade.springbootwithkotlin.controllers.PersonController
import br.com.brunotrindade.springbootwithkotlin.data.vo.v1.PersonVO
import br.com.brunotrindade.springbootwithkotlin.exceptions.RequiredObjectIsNullException
import br.com.brunotrindade.springbootwithkotlin.exceptions.ResourceNotFoundException
import br.com.brunotrindade.springbootwithkotlin.mapper.DozerMapper
import br.com.brunotrindade.springbootwithkotlin.model.Person
import br.com.brunotrindade.springbootwithkotlin.repository.PersonRepository
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class PersonService(private val repository: PersonRepository) {

    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(): List<PersonVO> {
        logger.info("Finding all people")

        val result = repository.findAll()
        val vos = DozerMapper.parseListObjects(result, PersonVO::class.java)

        return vos.stream().map { person -> personLink(person) }.toList()
    }

    fun create(person: PersonVO?): PersonVO {
        if (person == null) throw RequiredObjectIsNullException()
        logger.info("Creating one person with name ${person.firstName}!")
        val entity = DozerMapper.parseObject(person, Person::class.java)

        val personVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)

        return personLink(personVO)
    }


    fun update(person: PersonVO?): PersonVO {
        if (person == null) throw RequiredObjectIsNullException()
        val entity = repository.findById(person.key)
            .orElseThrow {
                ResourceNotFoundException("No records found for this id!")
            }

        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.address = person.address
        entity.gender = person.gender

        logger.info("Updating one person with id ${person.key}!")
        val personVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)

        return personLink(personVO)
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
        logger.info("Finding one person with ID $id")

        val person = repository.findById(id)
            .orElseThrow {
                ResourceNotFoundException("No records found for this id!")
            }
        val personVO = DozerMapper.parseObject(person, PersonVO::class.java)

        return personLink(personVO)
    }

    private fun personLink(personVO: PersonVO): PersonVO {
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()

        return personVO.add(withSelfRel)
    }

}
