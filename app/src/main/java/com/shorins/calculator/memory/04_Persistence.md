# Сохранение состояния (Persistence)

В приложении реализовано два уровня сохранения состояния, чтобы обеспечить максимально комфортный пользовательский опыт:

1.  **Сохранение при изменении конфигурации** (например, поворот экрана).
2.  **Постоянное хранение** между запусками приложения.

## 1. Сохранение при повороте экрана (`ViewModel`)

Эта функциональность достигается благодаря архитектуре компонентов Android и `ViewModel`.

-   **Жизненный цикл `ViewModel`**: Экземпляр `CalculatorViewModel` привязан к жинненному циклу `MainActivity`. Когда Activity уничтожается и пересоздается при повороте экрана, система автоматически предоставляет тот же самый, уже существующий экземпляр `ViewModel`.
-   **Хранение состояния в `ViewModel`**: Поскольку переменная `state` является свойством `ViewModel`, она не уничтожается вместе с Activity. Ее значение просто "пережидает" поворот экрана.

```kotlin
// MainActivity.kt
val viewModel by viewModels<CalculatorViewModel>()
```

Этот механизм работает "из коробки" и не требует написания дополнительного кода для сохранения и восстановления. UI просто перерисовывается на основе актуального состояния, которое он получает от `ViewModel`.

## 2. Постоянное хранение (`SharedPreferences`)

Чтобы введенное выражение сохранялось даже после полного закрытия и перезапуска приложения, используется `SharedPreferences`.

Вся логика инкапсулирована в `CalculatorViewModel`.

### Настройка

-   **`AndroidViewModel`**: `CalculatorViewModel` наследуется от `AndroidViewModel` вместо обычного `ViewModel`. Это дает ему доступ к контексту приложения, необходимому для работы с `SharedPreferences`.

    ```kotlin
    class CalculatorViewModel(application: Application) : AndroidViewModel(application)
    ```

-   **Инициализация**: `SharedPreferences` инициализируется один раз при создании `ViewModel`.

    ```kotlin
    private val sharedPreferences = application.getSharedPreferences("CalculatorState", Context.MODE_PRIVATE)
    ```

### Загрузка состояния (`loadState`)

При инициализации `ViewModel` переменная `state` получает свое начальное значение из функции `loadState()`.

```kotlin
var state by mutableStateOf(loadState())
```

Функция `loadState()` читает сохраненные значения `number1`, `number2` и `operation` из `SharedPreferences`. Если ничего не сохранено, она возвращает пустое состояние по умолчанию.

```kotlin
private fun loadState(): CalculatorState {
    val number1 = sharedPreferences.getString("number1", "") ?: ""
    val number2 = sharedPreferences.getString("number2", "") ?: ""
    val operationSymbol = sharedPreferences.getString("operation", null)
    // ... логика преобразования символа в операцию
    return CalculatorState(number1, number2, operation)
}
```

### Сохранение состояния (`saveState`)

Сохранение происходит автоматически после выполнения любого действия пользователя. Это достигается одним вызовом функции `saveState()` в конце метода `onAction()`.

```kotlin
fun onAction(action: CalculatorAction) {
    when (action) {
        // ... обработка всех действий
    }
    saveState(state) // <--- Сохранение здесь
}

private fun saveState(state: CalculatorState) {
    sharedPreferences.edit().apply {
        putString("number1", state.number1)
        putString("number2", state.number2)
        putString("operation", state.operation?.symbol)
        apply()
    }
}
```

Этот подход гарантирует, что любое изменение состояния будет немедленно записано в постоянное хранилище, и пользователь сможет продолжить работу с того же места, на котором остановился.
