package br.com.brunotrindade.springbootwithkotlin.services

import br.com.brunotrindade.springbootwithkotlin.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class UserService(private val repository: UserRepository) : UserDetailsService {

    private val logger = Logger.getLogger(UserService::class.java.name)

    override fun loadUserByUsername(username: String?): UserDetails {
        logger.info("Finding one User by Username $username")

        val user = repository.findByUsername(username)

        return user ?: throw UsernameNotFoundException("Username $username not found")
    }
}
