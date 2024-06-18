package dev.maxsiomin.common.util

import dev.maxsiomin.common.BuildConfig

fun isDebug() = BuildConfig.DEBUG

fun isRelease() = !isDebug()