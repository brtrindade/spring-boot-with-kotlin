package br.com.brunotrindade.springbootwithkotlin.math

import kotlin.math.sqrt

class SimpleMath {
    fun sum(numberOne: Double, numberTwo: Double) = numberOne + numberTwo
    fun subtract(numberOne: Double, numberTwo: Double) = numberOne - numberTwo
    fun multiply(numberOne: Double, numberTwo: Double) = numberOne * numberTwo
    fun divide(numberOne: Double, numberTwo: Double) = numberOne / numberTwo
    fun mean(numberOne: Double, numberTwo: Double) = (numberOne + numberTwo) / 2
    fun squareRoot(number: Double) = sqrt(number)
}
