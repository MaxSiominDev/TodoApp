package dev.maxsiomin.common.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import dev.maxsiomin.common.R

/** Represents info to construct a snackbar */
data class SnackbarInfo(
    val message: UiText,
    val action: UiText = UiText.StringResource(R.string.hide),
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val dismissPreviousSnackbarImmediately: Boolean = false,
    val onResult: ((SnackbarResult) -> Unit)? = null,
)

typealias SnackbarCallback = (SnackbarInfo) -> Unit
