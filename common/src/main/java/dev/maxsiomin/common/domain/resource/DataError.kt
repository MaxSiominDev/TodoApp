package dev.maxsiomin.common.domain.resource

sealed interface DataError : Error

sealed interface LocalError : DataError {

    data class Unknown(val message: String?) : LocalError

}
