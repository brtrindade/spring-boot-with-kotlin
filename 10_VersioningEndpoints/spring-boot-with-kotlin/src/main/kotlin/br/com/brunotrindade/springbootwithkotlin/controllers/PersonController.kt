package br.com.brunotrindade.springbootwithkotlin.controllers

import br.com.brunotrindade.springbootwithkotlin.data.vo.v1.PersonVO
import br.com.brunotrindade.springbootwithkotlin.data.vo.v2.PersonVO as PersonVOV2
import br.com.brunotrindade.springbootwithkotlin.services.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/person")
class PersonController {

    @Autowired
    private lateinit var service: PersonService

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findAll(): List<PersonVO> {
        return service.findAll()
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody person: PersonVO): PersonVO {
        return service.create(person)
    }

    @PostMapping(value = ["/v2"], consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createV2(@RequestBody person: PersonVOV2): PersonVOV2 {
        return service.createV2(person)
    }

    @PutMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun update(@RequestBody person: PersonVO): PersonVO {
        return service.update(person)
    }

    @GetMapping(value = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findById(@PathVariable(value = "id") id: Long): PersonVO {
        return service.findById(id)
    }

    @DeleteMapping(value = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun delete(@PathVariable(value = "id") id: Long): ResponseEntity<*> {
        service.delete(id)

        return ResponseEntity.noContent().build<Any>()
    }
}