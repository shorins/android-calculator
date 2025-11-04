package com.shorins.calculator.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.abs
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
        if (state.operation == null) {
            if (state.number1.isNotBlank() && !state.number1.contains("%")) {
                state = state.copy(number1 = state.number1 + "%")
            }
        } else {
            if (state.number2.isNotBlank() && !state.number2.contains("%")) {
                state = state.copy(number2 = state.number2 + "%")
            }
        }
    }

    private fun enterNumber(number: Int) {
        if (state.operation == null) {
            if (state.number1.contains("%")) return // Не разрешать ввод после знака %
            if (state.number1.length >= MAX_NUM_LENGTH) return
            state = state.copy(number1 = state.number1 + number)
            return
        }
        if (state.number2.contains("%")) return // Не разрешать ввод после знака %
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
        val number1String = state.number1
        val number2String = state.number2

        // Сценарий 1: Вычисление для одного числа (например, "50% =")
        if (number2String.isBlank() && state.operation == null) {
            if (number1String.contains("%")) {
                val result = parseNumberWithPercentage(number1String)
                if (result != null) {
                    state = state.copy(number1 = formatResult(result), number2 = "", operation = null)
                }
            }
            return
        }

        // Сценарий 2: Полное выражение (например, "100 + 10% =")
        if (number1String.isNotBlank() && number2String.isNotBlank() && state.operation != null) {
            val num1 = if (number1String.contains("%")) {
                parseNumberWithPercentage(number1String)
            } else {
                number1String.toDoubleOrNull()
            }

            if (num1 == null) return // Ошибка парсинга

            val num2 = if (number2String.contains("%")) {
                parseNumberWithPercentage(number2String, num1, state.operation)
            } else {
                number2String.toDoubleOrNull()
            }

            if (num2 == null) return // Ошибка парсинга

            val result = when (state.operation) {
                is CalculatorOperation.Add -> num1 + num2
                is CalculatorOperation.Subtract -> num1 - num2
                is CalculatorOperation.Multiply -> num1 * num2
                is CalculatorOperation.Divide -> {
                    if (num2 == 0.0) {
                        state = state.copy(number1 = "Error", number2 = "", operation = null)
                        return
                    }
                    num1 / num2
                }
                null -> return
            }
            state = state.copy(
                number1 = formatResult(result),
                number2 = "",
                operation = null
            )
        }
    }

    private fun parseNumberWithPercentage(
        numberString: String,
        baseNumber: Double? = null,
        operation: CalculatorOperation? = null
    ): Double? {
        if (!numberString.endsWith("%")) return numberString.toDoubleOrNull()

        val rawNumber = numberString.dropLast(1).toDoubleOrNull() ?: return null
        val percentage = rawNumber / 100.0

        return if (baseNumber != null && operation != null) {
            when (operation) {
                is CalculatorOperation.Add, is CalculatorOperation.Subtract -> baseNumber * percentage
                is CalculatorOperation.Multiply, is CalculatorOperation.Divide -> percentage
            }
        } else {
            percentage // For single number percentage (e.g., 50% = 0.5)
        }
    }

    private fun formatResult(number: Double): String {
        val longValue = number.toLong()
        // Если число очень большое или очень маленькое (но не ноль), используем научную нотацию
        if (abs(number) > 999_999_999_999.0 || (abs(number) < 0.0001 && number != 0.0)) {
            return String.format("%.6E", number)
        }

        // Если это целое число в пределах Long, форматируем без десятичной части
        return if (number == longValue.toDouble()) {
            longValue.toString()
        } else {
            number.toString()
        }.take(15) // Ограничиваем длину для отображения
    }

    companion object {
        private const val MAX_NUM_LENGTH = 15
    }
}