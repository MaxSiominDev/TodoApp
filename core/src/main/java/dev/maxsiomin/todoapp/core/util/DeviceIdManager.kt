package dev.maxsiomin.todoapp.core.util

import android.content.SharedPreferences
import dev.maxsiomin.todoapp.core.data.PrefsKeys
import dev.maxsiomin.todoapp.core.domain.UuidGenerator
import javax.inject.Inject

interface DeviceIdManager {

    fun getDeviceId(): String

}

/** Gets device id on Android devices */
internal class AndroidDeviceIdManager @Inject constructor(
    private val uuidGenerator: UuidGenerator,
    private val prefs: SharedPreferences,
) : DeviceIdManager {

    var id: String? = prefs.getString(PrefsKeys.DEVICE_ID, null)

    override fun getDeviceId(): String {
        return id ?: run {
            val newId = uuidGenerator.generateUuid()
            prefs.edit().putString(PrefsKeys.DEVICE_ID, newId).apply()
            return@run newId.also { id = it }
        }
    }

}
