package dev.maxsiomin.common.domain.resource

/** Convinient error handling class widely used throughout the project */
fun <E : Error> Resource<*, E>.errorOrNull(): E? = when (this) {
    is Resource.Error -> this.error
    is Resource.Success -> null
}
