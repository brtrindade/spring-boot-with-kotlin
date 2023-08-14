package br.com.brunotrindade.springbootwithkotlin.integrationtests.vo

import jakarta.xml.bind.annotation.XmlRootElement
import java.util.Date

@XmlRootElement
data class TokenVO(
    var username: String? = null,
    var authenticated: Boolean? = null,
    var accessToken: String? = null,
    var refreshToken: String? = null,
    var created: Date? = null,
    var expiration: Date? = null,
)
