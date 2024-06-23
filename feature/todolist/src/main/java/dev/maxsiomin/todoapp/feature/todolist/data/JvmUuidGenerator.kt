package dev.maxsiomin.todoapp.feature.todolist.data

import dev.maxsiomin.todoapp.feature.todolist.domain.UuidGenerator
import java.util.UUID
import javax.inject.Inject

class JvmUuidGenerator @Inject constructor() : UuidGenerator {

    override fun generateUuid(): String {
        return UUID.randomUUID().toString()
    }

}