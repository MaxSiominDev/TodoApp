package dev.maxsiomin.common.presentation

import dev.maxsiomin.common.R
import dev.maxsiomin.common.domain.resource.DataError
import dev.maxsiomin.common.domain.resource.LocalError
import dev.maxsiomin.common.domain.resource.NetworkError

fun DataError.asUiText(): UiText {
    return when (this) {
        is LocalError -> this.asUiText()
        is NetworkError -> this.asUiText()
    }
}

private fun LocalError.asUiText(): UiText {
    return when (this) {
        LocalError.NotFound -> {
            UiText.StringResource(R.string.unknown_error)
        }
    }
}

private fun NetworkError.asUiText(): UiText {

    return when (this) {

        NetworkError.InvalidRequest -> {
            UiText.StringResource(R.string.bad_request)
        }

        NetworkError.NotFound -> {
            UiText.StringResource(R.string.not_found)
        }

        NetworkError.Redirected -> {
            UiText.StringResource(R.string.bad_request)
        }

        NetworkError.Unauthorized -> {
            UiText.StringResource(R.string.unauthorized)
        }

        NetworkError.Server -> {
            UiText.StringResource(R.string.server_error)
        }

        NetworkError.NoInternet -> {
            UiText.StringResource(R.string.no_internet)
        }

        is NetworkError.Unknown -> {
            message?.let {
                UiText.DynamicString(it)
            } ?: UiText.StringResource(
                R.string.unknown_error,
            )
        }

    }

}
