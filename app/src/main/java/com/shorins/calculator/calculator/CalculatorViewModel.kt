package com.shorins.calculator.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.sqrt

class CalculatorViewModel : ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Operation -> enterOperation(action.operation)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Delete -> performDeletion()
            is CalculatorAction.Clear -> state = CalculatorState()
            is CalculatorAction.Calculate -> performCalculation()
            is CalculatorAction.Percent -> performPercent()
            is CalculatorAction.SquareRoot -> performSquareRoot()
            is CalculatorAction.SignChange -> performSignChange()
        }
    }

    private fun performSignChange() {
        if (state.number2.isNotBlank()) {
            val number = state.number2.toDoubleOrNull() ?: return
            state = state.copy(number2 = formatResult(number * -1))
        } else if (state.number1.isNotBlank()) {
            val number = state.number1.toDoubleOrNull() ?: return
            state = state.copy(number1 = formatResult(number * -1))
        }
    }

    private fun performSquareRoot() {
        if (state.number1.isNotBlank()) {
            val number = state.number1.toDoubleOrNull() ?: return
            state = state.copy(
                number1 = formatResult(sqrt(number)),
                number2 = "",
                operation = null
            )
        }
    }

    private fun performPercent() {
        // Логика процента: 50 + 10% = 55
        val number1 = state.number1.toDoubleOrNull()
        val number2 = state.number2.toDoubleOrNull()

        // Сценарий 1: Введено только одно число (5 -> %)
        if (number1 != null && state.operation == null && state.number2.isBlank()) {
            val result = number1 / 100.0
            state = state.copy(number1 = formatResult(result))
            return
        }

        // Сценарий 2: Введено выражение (100 + 10 %)
        if (number1 != null && number2 != null) {
            val percentageValue = when(state.operation) {
                // Для сложения/вычитания: 100 + 10% -> 100 + (100 * 0.1) -> второе число становится 10
                is CalculatorOperation.Add, is CalculatorOperation.Subtract -> number1 * number2 / 100.0
                // Для умножения/деления: 100 * 10% -> 100 * 0.1 -> второе число становится 0.1
                is CalculatorOperation.Multiply, is CalculatorOperation.Divide -> number2 / 100.0
                null -> return // Безопасная проверка
            }
            state = state.copy(number2 = formatResult(percentageValue))
        }
    }

    private fun enterNumber(number: Int) {
        if (state.operation == null) {
            if (state.number1.length >= MAX_NUM_LENGTH) return
            state = state.copy(number1 = state.number1 + number)
            return
        }
        if (state.number2.length >= MAX_NUM_LENGTH) return
        state = state.copy(number2 = state.number2 + number)
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (state.number1.isNotBlank()) {
            state = state.copy(operation = operation)
        }
    }


    private fun enterDecimal() {
        if (state.operation == null && !state.number1.contains(".") && state.number1.isNotBlank()) {
            state = state.copy(number1 = state.number1 + ".")
            return
        }
        if (!state.number2.contains(".") && state.number2.isNotBlank()) {
            state = state.copy(number2 = state.number2 + ".")
        }
    }

    private fun performDeletion() {
        when {
            state.number2.isNotBlank() -> state = state.copy(
                number2 = state.number2.dropLast(1)
            )
            state.operation != null -> state = state.copy(
                operation = null
            )
            state.number1.isNotBlank() -> state = state.copy(
                number1 = state.number1.dropLast(1)
            )
        }
    }

    private fun performCalculation() {
        val number1 = state.number1.toDoubleOrNull()
        val number2 = state.number2.toDoubleOrNull()
        if (number1 != null && number2 != null) {
            val result = when (state.operation) {
                is CalculatorOperation.Add -> number1 + number2
                is CalculatorOperation.Subtract -> number1 - number2
                is CalculatorOperation.Multiply -> number1 * number2
                is CalculatorOperation.Divide -> number1 / number2
                null -> return
            }
            state = state.copy(
                number1 = formatResult(result),
                number2 = "",
                operation = null
            )
        }
    }

    private fun formatResult(number: Double): String {
        return if (number % 1.0 == 0.0) {
            number.toLong().toString()
        } else {
            number.toString()
        }.take(15) // Ограничиваем длину, чтобы не вылезать за экран
    }

    companion object {
        private const val MAX_NUM_LENGTH = 8
    }
}