package com.shorins.calculator.calculator

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shorins.calculator.calculator.components.CalculatorButton

@Composable
fun CalculatorScreen(
    state: CalculatorState,
    onAction: (CalculatorAction) -> Unit
) {
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            LandscapeCalculatorLayout(state = state, onAction = onAction)
        }
        else -> {
            PortraitCalculatorLayout(state = state, onAction = onAction)
        }
    }
}

@Composable
fun CalculatorDisplay(
    state: CalculatorState,
    modifier: Modifier = Modifier
) {
    val text = state.number1 + (state.operation?.symbol ?: "") + state.number2
    val scrollState = rememberScrollState()

    LaunchedEffect(text) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Text(
            text = text,
            textAlign = TextAlign.End,
            modifier = Modifier
                .horizontalScroll(scrollState),
            fontWeight = FontWeight.Light,
            fontSize = when {
                text.length > 12 -> 40.sp
                text.length > 9 -> 55.sp
                text.length > 6 -> 70.sp
                else -> 80.sp
            },
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
fun PortraitCalculatorLayout(
    state: CalculatorState,
    onAction: (CalculatorAction) -> Unit
) {
    val buttonSpacing = 8.dp

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
            CalculatorDisplay(state = state, modifier = Modifier.weight(1f))
            PortraitButtonGrid(onAction = onAction, state = state, buttonSpacing = buttonSpacing)
        }
    }
}

@Composable
fun LandscapeCalculatorLayout(state: CalculatorState, onAction: (CalculatorAction) -> Unit) {
    val buttonSpacing = 8.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CalculatorDisplay(state = state, modifier = Modifier.weight(0.35f))
            Box(modifier = Modifier.weight(0.65f)) {
                LandscapeButtonGrid(onAction = onAction, state = state, buttonSpacing = buttonSpacing)
            }
        }
    }
}

@Composable
fun PortraitButtonGrid(
    onAction: (CalculatorAction) -> Unit,
    state: CalculatorState,
    buttonSpacing: Dp
) {
    val orange = MaterialTheme.colorScheme.primary
    val numberColor = MaterialTheme.colorScheme.onBackground
    val functionColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)

    Column(verticalArrangement = Arrangement.spacedBy(buttonSpacing)) {
        val buttonModifier = Modifier.aspectRatio(1f).weight(1f)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            CalculatorButton(symbol = if (state.number1.isNotBlank()) "C" else "AC", modifier = buttonModifier, color = functionColor, onClick = { onAction(CalculatorAction.Clear) })
            CalculatorButton(symbol = "+/-", modifier = buttonModifier, color = functionColor, onClick = { onAction(CalculatorAction.SignChange) })
            CalculatorButton(symbol = "%", modifier = buttonModifier, color = functionColor, onClick = { onAction(CalculatorAction.Percent) })
            CalculatorButton(symbol = "÷", modifier = buttonModifier, color = orange, onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Divide)) })
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            CalculatorButton(symbol = "7", modifier = buttonModifier, color = numberColor, onClick = { onAction(CalculatorAction.Number(7)) })
            CalculatorButton(symbol = "8", modifier = buttonModifier, color = numberColor, onClick = { onAction(CalculatorAction.Number(8)) })
            CalculatorButton(symbol = "9", modifier = buttonModifier, color = numberColor, onClick = { onAction(CalculatorAction.Number(9)) })
            CalculatorButton(symbol = "×", modifier = buttonModifier, color = orange, onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Multiply)) })
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            CalculatorButton(symbol = "4", modifier = buttonModifier, color = numberColor, onClick = { onAction(CalculatorAction.Number(4)) })
            CalculatorButton(symbol = "5", modifier = buttonModifier, color = numberColor, onClick = { onAction(CalculatorAction.Number(5)) })
            CalculatorButton(symbol = "6", modifier = buttonModifier, color = numberColor, onClick = { onAction(CalculatorAction.Number(6)) })
            CalculatorButton(symbol = "-", modifier = buttonModifier, color = orange, onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Subtract)) })
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            CalculatorButton(symbol = "1", modifier = buttonModifier, color = numberColor, onClick = { onAction(CalculatorAction.Number(1)) })
            CalculatorButton(symbol = "2", modifier = buttonModifier, color = numberColor, onClick = { onAction(CalculatorAction.Number(2)) })
            CalculatorButton(symbol = "3", modifier = buttonModifier, color = numberColor, onClick = { onAction(CalculatorAction.Number(3)) })
            CalculatorButton(symbol = "+", modifier = buttonModifier, color = orange, onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Add)) })
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            CalculatorButton(symbol = "0", modifier = Modifier.aspectRatio(2f).weight(2f), color = numberColor, onClick = { onAction(CalculatorAction.Number(0)) })
            CalculatorButton(symbol = ".", modifier = buttonModifier, color = numberColor, onClick = { onAction(CalculatorAction.Decimal) })
            CalculatorButton(symbol = "=", modifier = buttonModifier, color = orange, onClick = { onAction(CalculatorAction.Calculate) })
        }
    }
}

@Composable
fun LandscapeButtonGrid(
    onAction: (CalculatorAction) -> Unit,
    state: CalculatorState,
    buttonSpacing: Dp
) {
    val orange = MaterialTheme.colorScheme.primary
    val numberColor = MaterialTheme.colorScheme.onBackground
    val functionColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
    val landscapeButtonFontSize = 22.sp

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(buttonSpacing)
    ) {
        val rowModifier = Modifier.fillMaxWidth().weight(1f)
        val buttonModifier = Modifier.fillMaxHeight().weight(1f)
        val zeroButtonModifier = Modifier.fillMaxHeight().weight(2f)

        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            CalculatorButton(symbol = if (state.number1.isNotBlank()) "C" else "AC", modifier = buttonModifier, color = functionColor, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Clear) })
            CalculatorButton(symbol = "+/-", modifier = buttonModifier, color = functionColor, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.SignChange) })
            CalculatorButton(symbol = "%", modifier = buttonModifier, color = functionColor, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Percent) })
            CalculatorButton(symbol = "÷", modifier = buttonModifier, color = orange, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Divide)) })
        }
        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            CalculatorButton(symbol = "7", modifier = buttonModifier, color = numberColor, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Number(7)) })
            CalculatorButton(symbol = "8", modifier = buttonModifier, color = numberColor, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Number(8)) })
            CalculatorButton(symbol = "9", modifier = buttonModifier, color = numberColor, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Number(9)) })
            CalculatorButton(symbol = "×", modifier = buttonModifier, color = orange, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Multiply)) })
        }
        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            CalculatorButton(symbol = "4", modifier = buttonModifier, color = numberColor, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Number(4)) })
            CalculatorButton(symbol = "5", modifier = buttonModifier, color = numberColor, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Number(5)) })
            CalculatorButton(symbol = "6", modifier = buttonModifier, color = numberColor, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Number(6)) })
            CalculatorButton(symbol = "-", modifier = buttonModifier, color = orange, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Subtract)) })
        }
        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            CalculatorButton(symbol = "1", modifier = buttonModifier, color = numberColor, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Number(1)) })
            CalculatorButton(symbol = "2", modifier = buttonModifier, color = numberColor, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Number(2)) })
            CalculatorButton(symbol = "3", modifier = buttonModifier, color = numberColor, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Number(3)) })
            CalculatorButton(symbol = "+", modifier = buttonModifier, color = orange, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Add)) })
        }
        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            CalculatorButton(symbol = "0", modifier = zeroButtonModifier, color = numberColor, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Number(0)) })
            CalculatorButton(symbol = ".", modifier = buttonModifier, color = numberColor, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Decimal) })
            CalculatorButton(symbol = "=", modifier = buttonModifier, color = orange, fontSize = landscapeButtonFontSize, onClick = { onAction(CalculatorAction.Calculate) })
        }
    }
}