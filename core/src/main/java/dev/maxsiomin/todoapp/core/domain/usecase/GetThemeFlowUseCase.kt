package dev.maxsiomin.todoapp.core.domain.usecase

import dev.maxsiomin.todoapp.core.domain.Theme
import dev.maxsiomin.todoapp.core.domain.repository.ConfigRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeFlowUseCase @Inject constructor(private val repo: ConfigRepository) {

    suspend operator fun invoke(): Flow<Theme> {
        return repo.getThemeFlow()
    }

}
