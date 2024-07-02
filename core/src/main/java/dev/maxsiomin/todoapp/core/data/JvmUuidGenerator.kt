package dev.maxsiomin.todoapp.core.data

import dev.maxsiomin.todoapp.core.domain.UuidGenerator
import java.util.UUID
import javax.inject.Inject

class JvmUuidGenerator @Inject constructor() : UuidGenerator {

    override fun generateUuid(): String {
        return UUID.randomUUID().toString()
    }

}