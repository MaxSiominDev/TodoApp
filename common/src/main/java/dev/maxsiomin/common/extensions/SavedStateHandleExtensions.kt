package dev.maxsiomin.common.extensions

import androidx.lifecycle.SavedStateHandle

fun <T : Any> SavedStateHandle.requireArg(key: String): T {
    return requireNotNull(get<T>(key)) {
        "Argument $key is required but was not found in the savedStateHandle"
    }
}

fun <T> SavedStateHandle.safeArg(key: String): T? {
    return get<T>(key)
}
