package dev.maxsiomin.todoapp.core.domain.usecase

import dev.maxsiomin.todoapp.core.domain.repository.TokenRepository
import javax.inject.Inject

class SetTokenUseCase @Inject constructor(
    private val repo: TokenRepository,
) {

    operator fun invoke(token: String) {
        repo.setToken(token)
    }

}