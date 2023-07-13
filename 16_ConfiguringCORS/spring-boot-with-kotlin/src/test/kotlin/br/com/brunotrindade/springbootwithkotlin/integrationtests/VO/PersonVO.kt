package br.com.brunotrindade.springbootwithkotlin.integrationtests.VO

data class PersonVO(
    var id: Long = 0,
    var firstName: String = "",
    var lastName: String = "dependency",
    var address: String = "",
    var gender: String = ""
)
