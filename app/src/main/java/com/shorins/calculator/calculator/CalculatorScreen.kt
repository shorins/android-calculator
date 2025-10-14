package com.shorins.calculator.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shorins.calculator.calculator.components.CalculatorButton

@Composable
fun CalculatorScreen(
    state: CalculatorState,
    onAction: (CalculatorAction) -> Unit
) {
    val buttonSpacing = 8.dp
    val orange = MaterialTheme.colorScheme.primary
    val numberColor = MaterialTheme.colorScheme.onBackground
    val functionColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f) // Сделаем чуть бледнее

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            Text(
                text = state.number1 + (state.operation?.symbol ?: "") + state.number2,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                fontWeight = FontWeight.Light,
                fontSize = 80.sp,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2
            )
            // Ряд 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(symbol = if (state.number1.isNotBlank()) "C" else "AC", modifier = Modifier.aspectRatio(1f).weight(1f), color = functionColor, onClick = { onAction(CalculatorAction.Clear) })
                CalculatorButton(symbol = "+/-", modifier = Modifier.aspectRatio(1f).weight(1f), color = functionColor, onClick = { onAction(CalculatorAction.SignChange) })
                CalculatorButton(symbol = "%", modifier = Modifier.aspectRatio(1f).weight(1f), color = functionColor, onClick = { onAction(CalculatorAction.Percent) })
                CalculatorButton(symbol = "÷", modifier = Modifier.aspectRatio(1f).weight(1f), color = orange, onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Divide)) })
            }
            // Ряд 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(symbol = "7", modifier = Modifier.aspectRatio(1f).weight(1f), color = numberColor, onClick = { onAction(CalculatorAction.Number(7)) })
                CalculatorButton(symbol = "8", modifier = Modifier.aspectRatio(1f).weight(1f), color = numberColor, onClick = { onAction(CalculatorAction.Number(8)) })
                CalculatorButton(symbol = "9", modifier = Modifier.aspectRatio(1f).weight(1f), color = numberColor, onClick = { onAction(CalculatorAction.Number(9)) })
                CalculatorButton(symbol = "×", modifier = Modifier.aspectRatio(1f).weight(1f), color = orange, onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Multiply)) })
            }
            // Ряд 3
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(symbol = "4", modifier = Modifier.aspectRatio(1f).weight(1f), color = numberColor, onClick = { onAction(CalculatorAction.Number(4)) })
                CalculatorButton(symbol = "5", modifier = Modifier.aspectRatio(1f).weight(1f), color = numberColor, onClick = { onAction(CalculatorAction.Number(5)) })
                CalculatorButton(symbol = "6", modifier = Modifier.aspectRatio(1f).weight(1f), color = numberColor, onClick = { onAction(CalculatorAction.Number(6)) })
                CalculatorButton(symbol = "-", modifier = Modifier.aspectRatio(1f).weight(1f), color = orange, onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Subtract)) })
            }
            // Ряд 4
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(symbol = "1", modifier = Modifier.aspectRatio(1f).weight(1f), color = numberColor, onClick = { onAction(CalculatorAction.Number(1)) })
                CalculatorButton(symbol = "2", modifier = Modifier.aspectRatio(1f).weight(1f), color = numberColor, onClick = { onAction(CalculatorAction.Number(2)) })
                CalculatorButton(symbol = "3", modifier = Modifier.aspectRatio(1f).weight(1f), color = numberColor, onClick = { onAction(CalculatorAction.Number(3)) })
                CalculatorButton(symbol = "+", modifier = Modifier.aspectRatio(1f).weight(1f), color = orange, onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Add)) })
            }
            // Ряд 5
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(symbol = "0", modifier = Modifier.aspectRatio(2f).weight(2f), color = numberColor, onClick = { onAction(CalculatorAction.Number(0)) })
                CalculatorButton(symbol = ".", modifier = Modifier.aspectRatio(1f).weight(1f), color = numberColor, onClick = { onAction(CalculatorAction.Decimal) })
                CalculatorButton(symbol = "=", modifier = Modifier.aspectRatio(1f).weight(1f), color = orange, onClick = { onAction(CalculatorAction.Calculate) })
            }
        }
    }
}