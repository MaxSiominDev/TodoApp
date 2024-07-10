package dev.maxsiomin.common.presentation

import androidx.compose.material3.SnackbarResult
import dev.maxsiomin.common.R

/** Represents info to construct a snackbar */
data class SnackbarInfo(
    val message: UiText,
    val action: UiText = UiText.StringResource(R.string.hide),
    val onResult: ((SnackbarResult) -> Unit)? = null,
)

typealias SnackbarCallback = (SnackbarInfo) -> Unit
