package fyi.manpreet.portfolio.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import fyi.manpreet.portfolio.ui.todo.TodoDetailScreen
import fyi.manpreet.portfolio.ui.todo.TodoScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
) {

    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.TodoList::class, Route.TodoList.serializer())
                    subclass(Route.TodoDetail::class, Route.TodoDetail.serializer())
                }
            }
        },
        Route.TodoList
    )

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = { key ->
            when (key) {
                is Route.TodoList -> {
                    NavEntry(key) {
                        TodoScreen(
                            onTodoClick = {
                                backStack.add(Route.TodoDetail(it))
                            }
                        )
                    }
                }
                is Route.TodoDetail -> {
                    NavEntry(key) {
                        TodoDetailScreen(
                            todo = key.todo
                        )
                    }
                }
                else -> error("Unknown NavKey: $key")
            }
        }
    )

}