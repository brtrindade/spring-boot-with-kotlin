package br.com.brunotrindade.springbootwithkotlin.repository

import br.com.brunotrindade.springbootwithkotlin.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, Long?>