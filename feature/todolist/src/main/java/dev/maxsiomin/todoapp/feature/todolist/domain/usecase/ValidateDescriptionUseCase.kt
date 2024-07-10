package dev.maxsiomin.todoapp.feature.todolist.domain.usecase

import dev.maxsiomin.common.domain.resource.Error
import dev.maxsiomin.common.domain.resource.Resource
import javax.inject.Inject

/** Validates item description */
internal class ValidateDescriptionUseCase @Inject constructor() {

    operator fun invoke(description: String): Resource<Unit, DescriptionError> {
        if (description.isBlank()) {
            return Resource.Error(DescriptionError.Empty)
        }
        return Resource.Success(Unit)
    }

    sealed interface DescriptionError : Error {
        data object Empty : DescriptionError
    }

}