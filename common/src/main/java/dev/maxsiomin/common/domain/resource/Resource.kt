package dev.maxsiomin.common.domain.resource

typealias RootError = Error

sealed interface Resource<out D, out E: RootError> {
    data class Success<out D, out E: RootError>(val data: D): Resource<D, E>
    data class Error<out D, out E: RootError>(val error: E): Resource<D, E>
}
