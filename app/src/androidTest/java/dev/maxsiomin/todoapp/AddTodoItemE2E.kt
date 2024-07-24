package dev.maxsiomin.todoapp

import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.maxsiomin.todoapp.core.presentation.theme.AppTheme
import dev.maxsiomin.todoapp.feature.todolist.R
import dev.maxsiomin.todoapp.ui.TodoApp
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.toByteArray
import io.ktor.http.HttpMethod
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID
import javax.inject.Inject

@HiltAndroidTest
@LargeTest
class AddTodoItemE2E {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private fun stringResource(@StringRes res: Int) = composeTestRule.activity.getString(res)

    @Inject
    lateinit var mockEngine: MockEngine

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun addTodoItem() = runBlocking {

        composeTestRule.setContent {
            AppTheme(isDarkTheme = false) {
                val appState = rememberTodoAppState(isAuthenticated = true)
                TodoApp(appState)
            }
        }

        composeTestRule.onNodeWithContentDescription(stringResource(R.string.add_todo)).performClick()

        val itemDescription = UUID.randomUUID().toString()
        composeTestRule.onNodeWithTag("description text field").performTextInput(itemDescription)
        composeTestRule.onNodeWithText(stringResource(R.string.save)).performClick()

        composeTestRule.onNodeWithContentDescription(stringResource(R.string.add_todo)).assertExists()
        composeTestRule.onNodeWithText(itemDescription).assertExists()

        val postRequests = mockEngine.requestHistory.filter {
            it.method == HttpMethod.Post
        }.filter {
            it.url.toString() == "https://hive.mrdekk.ru/todo/list"
        }.filter {
            it.body.toByteArray().decodeToString().contains(itemDescription)
        }
        assertThat(postRequests).isNotEmpty()
    }

}