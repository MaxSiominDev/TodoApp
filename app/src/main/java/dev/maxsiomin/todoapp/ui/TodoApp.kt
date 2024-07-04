package dev.maxsiomin.todoapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.maxsiomin.common.presentation.SnackbarInfo
import dev.maxsiomin.todoapp.TodoAppState
import kotlinx.coroutines.launch

@Composable
internal fun TodoApp(appState: TodoAppState) {

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val showSnackbar: (SnackbarInfo) -> Unit = remember {
        { info: SnackbarInfo ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = info.message.asString(context),
                    actionLabel = info.action.asString(context),
                    duration = SnackbarDuration.Short,
                ).also { result ->
                    info.onResult?.invoke(result)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TodoappNavHost(
                appState = appState,
                showSnackbar = showSnackbar,
            )
        }
    }

}
