package dev.maxsiomin.todoapp.feature.todolist.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.maxsiomin.common.domain.resource.Resource
import org.junit.Test

class ValidateDescriptionUseCaseTest {

    private val useCase = ValidateDescriptionUseCase()

    @Test
    fun `any non-blanc description returns success`() {
        val description = "hello world"
        val result = useCase(description)
        assertThat(result).isInstanceOf(Resource.Success::class.java)
    }

    @Test
    fun `non-empty blanc description returns error`() {
        val description = "   "
        val result = useCase(description)
        assertThat(result).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `empty description returns error`() {
        val description = ""
        val result = useCase(description)
        assertThat(result).isInstanceOf(Resource.Error::class.java)
    }

}