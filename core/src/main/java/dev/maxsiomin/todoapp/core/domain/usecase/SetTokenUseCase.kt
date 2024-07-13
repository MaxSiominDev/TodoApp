package dev.maxsiomin.todoapp.core.domain.usecase

import dev.maxsiomin.todoapp.core.domain.repository.ConfigRepository
import javax.inject.Inject

/** Saves Yandex API token */
class SetTokenUseCase @Inject constructor(
    private val repo: ConfigRepository,
) {

    operator fun invoke(token: String) {
        repo.setToken(token)
    }

}