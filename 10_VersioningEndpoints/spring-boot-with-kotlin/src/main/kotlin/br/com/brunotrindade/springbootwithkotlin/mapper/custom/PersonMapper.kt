package br.com.brunotrindade.springbootwithkotlin.mapper.custom

import br.com.brunotrindade.springbootwithkotlin.data.vo.v2.PersonVO
import br.com.brunotrindade.springbootwithkotlin.model.Person
import org.springframework.stereotype.Service
import java.util.*

@Service
class PersonMapper {

    fun mapEntityToVO(person: Person): PersonVO {
        val vo = PersonVO()
        vo.id = person.id
        vo.address = person.address
        vo.birthday = Date()
        vo.firstName = person.firstName
        vo.lastName = person.lastName
        vo.gender = person.gender
        return vo
    }

    fun mapVOToEntity(person: PersonVO): Person {
        val entity = Person()
        entity.id = person.id
        entity.address = person.address
        // entity.birthday = person.birthday
        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.gender = person.gender
        return entity
    }
}
