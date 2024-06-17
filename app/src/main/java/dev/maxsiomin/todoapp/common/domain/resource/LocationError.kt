package dev.maxsiomin.todoapp.common.domain.resource

sealed interface LocationError : Error {

    data object GpsDisabled : LocationError
    data object MissingPermission : LocationError
    data object Unknown : LocationError

}