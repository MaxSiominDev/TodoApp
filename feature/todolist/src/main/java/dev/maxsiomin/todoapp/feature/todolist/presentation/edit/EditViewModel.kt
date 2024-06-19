package dev.maxsiomin.todoapp.feature.todolist.presentation.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.common.domain.resource.Resource
import dev.maxsiomin.common.extensions.now
import dev.maxsiomin.common.extensions.safeArg
import dev.maxsiomin.common.presentation.StatefulViewModel
import dev.maxsiomin.todoapp.core.util.DateFormatter
import dev.maxsiomin.todoapp.feature.todolist.domain.UuidGenerator
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Priority
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Progress
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import dev.maxsiomin.todoapp.feature.todolist.domain.usecase.AddTodoItemUseCase
import dev.maxsiomin.todoapp.feature.todolist.domain.usecase.DeleteTodoItemUseCase
import dev.maxsiomin.todoapp.feature.todolist.domain.usecase.GetTodoItemByIdUseCase
import dev.maxsiomin.todoapp.navdestinations.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import javax.inject.Inject

@HiltViewModel
internal class EditViewModel @Inject constructor(
    private val addTodoItemUseCase: AddTodoItemUseCase,
    private val getTodoItemByIdUseCase: GetTodoItemByIdUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
    private val dateFormatter: DateFormatter,
    private val uuidGenerator: UuidGenerator,
    savedStateHandle: SavedStateHandle,
) : StatefulViewModel<EditViewModel.State, EditViewModel.Effect, EditViewModel.Event>() {

    private val itemId: String? = savedStateHandle.safeArg(Screen.EditScreen.Args.itemId)

    private var todoItem: TodoItem? = null

    private var isSavingOrDeleting = false

    data class State(
        val description: String,
        val deadlineDate: LocalDate,
        val deadlineStringDate: String,
        val deadLineSwitchIsOn: Boolean,
        val showSelectDeadlineDateDialog: Boolean = false,
        val priority: Priority,
        val priorityDropdownExpanded: Boolean = false,
    )

    override val _state: MutableStateFlow<State>

    init {
        val date = getDefaultDeadlineDate()
        _state = MutableStateFlow(
            State(
                description = "",
                deadlineDate = date,
                deadlineStringDate = dateFormatter.formatDate(date),
                deadLineSwitchIsOn = false,
                priority = Priority.Default,
            )
        )
        if (itemId != null) {
            viewModelScope.launch {
                when (val resource = getTodoItemByIdUseCase(itemId)) {
                    is Resource.Error -> Unit
                    is Resource.Success -> {
                        val item = resource.data
                        processTodoItem(item)
                    }
                }
            }
        }
    }

    private fun processTodoItem(item: TodoItem) {
        todoItem = item
        _state.update {
            val deadlineDate = item.deadline ?: getDefaultDeadlineDate()
            val deadlineStringDate = dateFormatter.formatDate(deadlineDate)
            State(
                description = item.description,
                priority = item.priority,
                deadlineDate = deadlineDate,
                deadlineStringDate = deadlineStringDate,
                deadLineSwitchIsOn = item.deadline != null,
            )
        }
    }

    sealed class Effect {
        data object GoBack : Effect()
    }

    sealed class Event {
        data class OnDescriptionChanged(val newValue: String) : Event()
        data class DeadlineSwitchChecked(val newValue: Boolean) : Event()
        data object DeleteIconClicked : Event()
        data object SaveClicked : Event()
        data class NewDeadlineDateSelected(val newDate: LocalDate) : Event()
        data object SelectDeadlineDateClicked : Event()
        data object ExpandPriorityDropdown : Event()
        data object CollapsePriorityDropdown : Event()
        data class NewPrioritySelected(val newPriority: Priority) : Event()
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.OnDescriptionChanged -> _state.update {
                it.copy(description = event.newValue)
            }

            is Event.DeadlineSwitchChecked -> _state.update {
                it.copy(deadLineSwitchIsOn = event.newValue)
            }

            Event.DeleteIconClicked -> onDelete()

            Event.SaveClicked -> onSave()

            is Event.NewDeadlineDateSelected -> onNewDate(event.newDate)

            Event.SelectDeadlineDateClicked -> _state.update {
                it.copy(showSelectDeadlineDateDialog = true)
            }

            Event.ExpandPriorityDropdown -> _state.update {
                it.copy(priorityDropdownExpanded = true)
            }

            Event.CollapsePriorityDropdown -> _state.update {
                it.copy(priorityDropdownExpanded = false)
            }

            is Event.NewPrioritySelected -> _state.update {
                it.copy(priority = event.newPriority, priorityDropdownExpanded = false)
            }
        }
    }

    private fun onSave() {
        if (isSavingOrDeleting) return
        isSavingOrDeleting = true
        viewModelScope.launch {
            todoItem?.let { todoItem ->
                saveEditedTodoItem(todoItem, state.value)
            } ?: saveNewTodoItem(state.value)
            onEffect(Effect.GoBack)
        }
    }

    private suspend fun saveEditedTodoItem(todoItem: TodoItem, state: State) {
        val deadline: LocalDate? = if (state.deadLineSwitchIsOn) state.deadlineDate else null
        val modified: LocalDate = LocalDate.now()
        val priority = state.priority
        val description = state.description
        val newItem = todoItem.copy(
            description = description,
            deadline = deadline,
            modified = modified,
            priority = priority,
        )
        addTodoItemUseCase(newItem)
    }

    private suspend fun saveNewTodoItem(state: State) {
        val deadline: LocalDate? = if (state.deadLineSwitchIsOn) state.deadlineDate else null
        val modified = null
        val priority = state.priority
        val description = state.description
        val uuid = uuidGenerator.generateUuid()
        val created = LocalDate.now()
        val newItem = TodoItem(
            id = uuid,
            description = description,
            priority = priority,
            created = created,
            deadline = deadline,
            progress = Progress.NotCompleted,
            modified = modified,
        )
        addTodoItemUseCase(newItem)
    }

    private fun onDelete() {
        if (isSavingOrDeleting) return
        isSavingOrDeleting = true
        viewModelScope.launch {
            // If is editing an item, then delete it, otherwise just go back
            todoItem?.let {
                deleteTodoItemUseCase(it)
            }
            onEffect(Effect.GoBack)
        }
    }

    private fun onNewDate(newDate: LocalDate) = _state.update {
        it.copy(
            deadlineDate = newDate,
            deadlineStringDate = dateFormatter.formatDate(newDate),
            showSelectDeadlineDateDialog = false,
        )
    }

    private fun getDefaultDeadlineDate(): LocalDate {
        return LocalDate.now().plus(1, DateTimeUnit.DAY)
    }

}
