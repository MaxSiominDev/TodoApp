package dev.maxsiomin.todoapp.core.domain.usecase

import dev.maxsiomin.todoapp.core.domain.Theme
import dev.maxsiomin.todoapp.core.domain.repository.ConfigRepository
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(private val repo: ConfigRepository) {

    operator fun invoke(theme: Theme) {
        repo.saveTheme(theme)
    }

}
