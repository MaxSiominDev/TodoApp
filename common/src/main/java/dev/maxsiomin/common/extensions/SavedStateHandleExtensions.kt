package dev.maxsiomin.common.extensions

import androidx.lifecycle.SavedStateHandle

fun <T> SavedStateHandle.requireArg(key: String): T {
    return requireNotNull(get<T>(key)) {
        "Argument $key is required but was not found in the savedStateHandle"
    }
}