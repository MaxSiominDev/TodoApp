package dev.maxsiomin.common.domain.resource

fun <E : Error> Resource<*, E>.errorOrNull(): E? = when (this) {
    is Resource.Error -> this.error
    is Resource.Success -> null
}
