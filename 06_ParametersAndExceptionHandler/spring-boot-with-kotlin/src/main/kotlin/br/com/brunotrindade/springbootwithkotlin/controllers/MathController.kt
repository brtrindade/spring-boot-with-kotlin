package br.com.brunotrindade.springbootwithkotlin.controllers

import br.com.brunotrindade.springbootwithkotlin.converters.NumberConverter
import br.com.brunotrindade.springbootwithkotlin.exceptions.UnsupportedMathOperationException
import br.com.brunotrindade.springbootwithkotlin.math.SimpleMath
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.sqrt

@RestController
class MathController {

    val counter: AtomicLong = AtomicLong()
    private val math: SimpleMath = SimpleMath()

    @RequestMapping(value = ["/sum/{numberOne}/{numberTwo}"])
    fun sum(@PathVariable(value = "numberOne") numberOne: String?,
            @PathVariable(value = "numberTwo") numberTwo: String?
    ): Double {
        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo))
            throw UnsupportedMathOperationException("Please set a numeric value!")
        return math.sum(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo))
    }

    @RequestMapping(value = ["/subtraction/{numberOne}/{numberTwo}"])
    fun subtraction(@PathVariable(value = "numberOne") numberOne: String?,
                    @PathVariable(value = "numberTwo") numberTwo: String?
    ): Double {
        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo))
            throw UnsupportedMathOperationException("Please set a numeric value!")
        return math.subtract(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo))
    }

    @RequestMapping(value = ["/multiply/{numberOne}/{numberTwo}"])
    fun multiply(@PathVariable(value = "numberOne") numberOne: String?,
                 @PathVariable(value = "numberTwo") numberTwo: String?
    ): Double {
        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo))
            throw UnsupportedMathOperationException("Please set a numeric value!")
        return math.multiply(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo))
    }

    @RequestMapping(value = ["/divide/{numberOne}/{numberTwo}"])
    fun divide(@PathVariable(value = "numberOne") numberOne: String?,
               @PathVariable(value = "numberTwo") numberTwo: String?
    ): Double {
        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo))
            throw UnsupportedMathOperationException("Please set a numeric value!")
        return math.divide(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo))
    }

    @RequestMapping(value = ["/mean/{numberOne}/{numberTwo}"])
    fun mean(@PathVariable(value = "numberOne") numberOne: String?,
             @PathVariable(value = "numberTwo") numberTwo: String?
    ): Double {
        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo))
            throw UnsupportedMathOperationException("Please set a numeric value!")
        return math.mean(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo))
    }

    @RequestMapping(value = ["/squareroot/{number}"])
    fun squareRoot(@PathVariable(value = "number") number: String?,
    ): Double {
        if (!NumberConverter.isNumeric(number)) throw UnsupportedMathOperationException("Please set a numeric value!")
        return math.squareRoot(NumberConverter.convertToDouble(number))
    }
}