# UI и Рендеринг

Пользовательский интерфейс приложения полностью построен на Jetpack Compose — декларативном UI-фреймворке от Google.

## 1. Точка входа: `MainActivity.kt`

Все начинается в `MainActivity`. Здесь создается `CalculatorViewModel` и вызывается главный Composable-экран `CalculatorScreen`.

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel by viewModels<CalculatorViewModel>()
            val state = viewModel.state
            CalculatorScreen(
                state = state,
                onAction = viewModel::onAction
            )
        }
    }
}
```

-   `by viewModels<CalculatorViewModel>()`: Этот делегат создает и предоставляет экземпляр `CalculatorViewModel`. Важно, что этот экземпляр переживает изменения конфигурации (например, поворот экрана).
-   `viewModel.state`: Получение текущего состояния UI из ViewModel.
-   `viewModel::onAction`: Передача функции-обработчика действий пользователя в UI-слой.

## 2. `CalculatorScreen.kt`: Управление отображением

Это главный UI-файл. Его основная задача — определить ориентацию устройства и нарисовать соответствующий макет.

```kotlin
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
```

-   `LocalConfiguration.current`: Позволяет получить доступ к текущей конфигурации устройства, включая ориентацию экрана.
-   В зависимости от ориентации, он вызывает либо `LandscapeCalculatorLayout`, либо `PortraitCalculatorLayout`.

### Переиспользуемые компоненты

-   **`CalculatorDisplay`**: Отвечает за отображение строки ввода/результата. Он включает в себя логику для автоматического уменьшения размера шрифта и горизонтальной прокрутки для длинных выражений.
-   **`PortraitButtonGrid` / `LandscapeButtonGrid`**: Отвечают за отрисовку сетки кнопок для каждой ориентации. Ключевое различие между ними — в используемых модификаторах для кнопок, что позволяет им выглядеть по-разному в портретном (квадратные кнопки) и ландшафтном (широкие, овальные кнопки) режимах.

## 3. Логика перерисовки при повороте

Когда пользователь поворачивает экран, происходит следующее:

1.  `MainActivity` пересоздается системой Android.
2.  Благодаря `by viewModels()`, Activity получает тот же самый, уже существующий экземпляр `CalculatorViewModel`. Состояние калькулятора, хранящееся в нем, не теряется.
3.  `setContent` вызывается заново.
4.  `CalculatorScreen` получает актуальное состояние из `ViewModel`.
5.  `LocalConfiguration.current` теперь сообщает новую ориентацию (например, `ORIENTATION_LANDSCAPE`).
6.  `when` блок выбирает и отрисовывает соответствующий макет (`LandscapeCalculatorLayout`), который использует то же самое состояние, что было до поворота.

Таким образом, перерисовка происходит автоматически и с сохранением всех данных благодаря архитектуре ViewModel и реактивности Jetpack Compose.
