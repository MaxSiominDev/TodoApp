package dev.maxsiomin.todoapp

import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
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
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID

@HiltAndroidTest
@LargeTest
class AddTodoItemE2E {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private fun stringResource(@StringRes res: Int) = composeTestRule.activity.getString(res)

    private var mockWebServer: MockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun addTodoItem() {
        mockWebServer.enqueue(MockResponse().setResponseCode(200))

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

        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("")
        assertThat(request.body.readUtf8()).contains(itemDescription)
        assertThat(request.method).isEqualTo("POST")

    }

}