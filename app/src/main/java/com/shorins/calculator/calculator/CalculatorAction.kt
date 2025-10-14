package com.shorins.calculator.calculator

sealed class CalculatorAction {
    data class Number(val number: Int) : CalculatorAction()
    object Clear : CalculatorAction()
    object Delete : CalculatorAction()
    object Decimal : CalculatorAction()
    object Calculate : CalculatorAction()
    data class Operation(val operation: CalculatorOperation) : CalculatorAction()
    object Percent : CalculatorAction()
    object SquareRoot : CalculatorAction()
    object SignChange : CalculatorAction()
}