# Управление состоянием и ViewModel

Центром всей логики приложения является `CalculatorViewModel`. Он реализует паттерн MVVM, отделяя логику отображения (View) от бизнес-логики (ViewModel).

## 1. `CalculatorViewModel`

Это "мозг" калькулятора. Его обязанности:

-   Хранить текущее состояние калькулятора.
-   Обрабатывать все действия пользователя.
-   Выполнять вычисления.
-   Управлять сохранением и загрузкой состояния.

## 2. `CalculatorState`: Единственный источник истины

Состояние всего экрана калькулятора описывается одним data-классом:

```kotlin
data class CalculatorState(
    val number1: String = "",
    val number2: String = "",
    val operation: CalculatorOperation? = null
)
```

-   `number1`: Первое число в выражении (или результат).
-   `number2`: Второе число в выражении.
-   `operation`: Выбранная математическая операция (`+`, `-` и т.д.).

В `ViewModel` состояние хранится в переменной, обернутой в `mutableStateOf`, что делает его наблюдаемым для Jetpack Compose.

```kotlin
var state by mutableStateOf(loadState())
    private set
```

-   `by mutableStateOf`: Делегат, который сообщает Compose, что при изменении `state` нужно перерисовать все UI-компоненты, которые его используют.
-   `private set`: Сеттер является приватным, что означает, что состояние может быть изменено только внутри `ViewModel`. Это обеспечивает однонаправленный поток данных (Unidirectional Data Flow).

## 3. `CalculatorAction`: Действия пользователя

Все возможные действия пользователя определены в виде запечатанного (sealed) класса. Это позволяет строго типизировать события, приходящие от UI.

```kotlin
sealed class CalculatorAction {
    data class Number(val number: Int) : CalculatorAction()
    object Clear : CalculatorAction()
    object Delete : CalculatorAction()
    // ... и другие действия
}
```

## 4. `onAction`: Центральный обработчик

Все события от UI (нажатия кнопок) приходят в единую функцию `onAction` в `ViewModel`.

```kotlin
fun onAction(action: CalculatorAction) {
    when (action) {
        is CalculatorAction.Number -> enterNumber(action.number)
        is CalculatorAction.Operation -> enterOperation(action.operation)
        is CalculatorAction.Clear -> state = CalculatorState()
        // ... обработка всех действий
    }
    saveState(state) // Сохранение состояния после каждого действия
}
```

Этот подход имеет несколько преимуществ:
-   **Централизация:** Вся логика обработки находится в одном месте.
-   **Тестируемость:** Легко тестировать `ViewModel`, вызывая функцию `onAction` с разными действиями и проверяя итоговое состояние.
-   **Предсказуемость:** Легко отслеживать, как и почему изменяется состояние приложения.
