package dev.maxsiomin.todoapp.feature.todolist.data.mappers

import com.google.common.truth.Truth.assertThat
import dev.maxsiomin.todoapp.feature.todolist.data.local.TodoItemEntity
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Priority
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import kotlinx.datetime.LocalDate
import org.junit.Test

class TodoItemMapperTest {

    private val mapper = TodoItemMapper()

    @Test
    fun `test toDomain mapping`() {
        // Given
        val entity = TodoItemEntity(
            id = "1",
            description = "Test description",
            priority = Priority.High,
            isCompleted = false,
            created = LocalDate(2024, 6, 25),
            modified = LocalDate(2024, 6, 26),
            deadline = LocalDate(2024, 7, 1),
        )

        // When
        val domain = mapper.toDomain(entity)

        // Then
        assertThat(domain.id).isEqualTo(entity.id)
        assertThat(domain.description).isEqualTo(entity.description)
        assertThat(domain.priority).isEqualTo(entity.priority)
        assertThat(domain.isCompleted).isEqualTo(entity.isCompleted)
        assertThat(domain.created).isEqualTo(entity.created)
        assertThat(domain.modified).isEqualTo(entity.modified)
        assertThat(domain.deadline).isEqualTo(entity.deadline)
    }

    @Test
    fun `test toData mapping`() {
        // Given
        val domain = TodoItem(
            id = "1",
            description = "Test description",
            priority = Priority.High,
            isCompleted = false,
            created = LocalDate(2024, 6, 25),
            modified = LocalDate(2024, 6, 26),
            deadline = LocalDate(2024, 7, 1)
        )

        // When
        val entity = mapper.toData(domain)

        // Then
        assertThat(entity.id).isEqualTo(domain.id)
        assertThat(entity.description).isEqualTo(domain.description)
        assertThat(entity.priority).isEqualTo(domain.priority)
        assertThat(entity.isCompleted).isEqualTo(domain.isCompleted)
        assertThat(entity.created).isEqualTo(domain.created)
        assertThat(entity.modified).isEqualTo(domain.modified)
        assertThat(entity.deadline).isEqualTo(domain.deadline)
    }

}
