package dev.maxsiomin.common.domain.resource

sealed interface DataError : Error

sealed interface NetworkError : DataError {

    data object InvalidRequest : NetworkError
    data object Unauthorized : NetworkError
    data object Redirected : NetworkError
    data object NotFound : NetworkError
    data object Server : NetworkError
    data object Unknown : NetworkError

}

sealed interface LocalError : DataError {

    data object NotFound : LocalError

}
