package br.com.brunotrindade.springbootwithkotlin.security.jwt

import br.com.brunotrindade.springbootwithkotlin.data.vo.v1.TokenVO
import br.com.brunotrindade.springbootwithkotlin.exceptions.InvalidJwtAuthenticationException
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*

@Service
class JwtTokenProvider(private val userDetailsService: UserDetailsService) {

    @Value("\${security.jwt.token.secret-key:secret}")
    private var secretKey = "secret"

    @Value("\${security.jwt.token.expire-length:3600000}")
    private var validInMilliseconds: Long = 3_600_000 // 1h

    private lateinit var algorithm: Algorithm

    @PostConstruct
    protected fun init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
        algorithm = Algorithm.HMAC256(secretKey.toByteArray())
    }

    fun createAccessToken(username: String, roles: List<String?>): TokenVO {
        val now = Date()
        val validity = Date(now.time + validInMilliseconds)
        val accessToken = getAccessToken(username, roles, now, validity)
        val refreshToken = getRefreshToken(username, roles, now)


        return TokenVO(
            username,
            true,
            accessToken,
            refreshToken,
            now,
            validity,
        )
    }


    fun refreshToken(refreshToken: String): TokenVO {
        var token = ""
        if (refreshToken.contains("Bearer ")) token = refreshToken.substring("Bearer ".length)
        val verifier: JWTVerifier = JWT.require(algorithm).build()
        val decodedJWT: DecodedJWT = verifier.verify(token)
        val username = decodedJWT.subject
        val roles: List<String> = decodedJWT.getClaim("roles").asList(String::class.java)

        return createAccessToken(username, roles)
    }


    private fun getAccessToken(username: String, roles: List<String?>, now: Date, validity: Date): String {
        val issuerURL: String = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()

        return JWT.create()
            .withClaim("roles", roles)
            .withIssuedAt(now)
            .withExpiresAt(validity)
            .withSubject(username)
            .withIssuer(issuerURL)
            .sign(algorithm)
            .trim()
    }


    private fun getRefreshToken(username: String, roles: List<String?>, now: Date): String {
        val validityRefreshToken = Date(now.time + validInMilliseconds * 3) // 3h

        return JWT.create()
            .withClaim("roles", roles)
            .withExpiresAt(validityRefreshToken)
            .withSubject(username)
            .sign(algorithm)
            .trim()
    }


    fun getAuthentication(token: String): Authentication {
        val decodedJWT: DecodedJWT = decodedToken(token)
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(decodedJWT.subject)

        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }

    private fun decodedToken(token: String): DecodedJWT {
        val algorithm = Algorithm.HMAC256(secretKey.toByteArray())
        val verifier: JWTVerifier = JWT.require(algorithm).build()

        return verifier.verify(token)
    }


    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")

        return if (!bearerToken.isNullOrBlank() && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring("Bearer ".length)
        } else null
    }


    fun validadeToken(token: String): Boolean {
        val decodedJWT = decodedToken(token)
        try {
            if (decodedJWT.expiresAt.before(Date())) return false
            return true
        } catch (e: Exception) {
            throw InvalidJwtAuthenticationException("Expired or invalid JWT Token!")
        }
    }
}
