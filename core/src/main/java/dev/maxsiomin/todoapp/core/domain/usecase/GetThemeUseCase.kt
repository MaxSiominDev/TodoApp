package dev.maxsiomin.todoapp.core.domain.usecase

import dev.maxsiomin.todoapp.core.domain.Theme
import dev.maxsiomin.todoapp.core.domain.repository.ConfigRepository
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(private val repo: ConfigRepository) {

    operator fun invoke(): Theme {
        return repo.getTheme()
    }

}
