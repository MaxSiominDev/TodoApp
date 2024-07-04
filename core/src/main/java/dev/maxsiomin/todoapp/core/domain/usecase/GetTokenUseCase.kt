package dev.maxsiomin.todoapp.core.domain.usecase

import dev.maxsiomin.todoapp.core.domain.repository.TokenRepository
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val repo: TokenRepository,
) {

    operator fun invoke(): String? {
        return repo.getToken()
    }

}