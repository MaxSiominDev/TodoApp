package dev.maxsiomin.todoapp.feature.todolist.presentation.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.common.domain.resource.Resource
import dev.maxsiomin.common.domain.resource.errorOrNull
import dev.maxsiomin.common.extensions.now
import dev.maxsiomin.common.extensions.safeArg
import dev.maxsiomin.common.extensions.toLocalizedDate
import dev.maxsiomin.common.presentation.StatefulViewModel
import dev.maxsiomin.common.presentation.UiText
import dev.maxsiomin.todoapp.feature.todolist.R
import dev.maxsiomin.todoapp.core.domain.UuidGenerator
import dev.maxsiomin.todoapp.core.util.DeviceIdManager
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Priority
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import dev.maxsiomin.todoapp.feature.todolist.domain.usecase.AddTodoItemUseCase
import dev.maxsiomin.todoapp.feature.todolist.domain.usecase.DeleteTodoItemUseCase
import dev.maxsiomin.todoapp.feature.todolist.domain.usecase.EditTodoItemUseCase
import dev.maxsiomin.todoapp.feature.todolist.domain.usecase.GetTodoItemByIdUseCase
import dev.maxsiomin.todoapp.feature.todolist.domain.usecase.ValidateDescriptionUseCase
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
    private val editTodoItemUseCase: EditTodoItemUseCase,
    private val getTodoItemByIdUseCase: GetTodoItemByIdUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
    private val validateDescriptionUseCase: ValidateDescriptionUseCase,
    private val uuidGenerator: UuidGenerator,
    private val deviceIdManager: DeviceIdManager,
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
                deadlineStringDate = date.toLocalizedDate(),
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
            val deadlineStringDate = deadlineDate.toLocalizedDate()
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
        data class ShowMessage(val message: UiText) : Effect()
        data class ShowToast(val message: UiText) : Effect()
    }

    sealed class Event {
        data class OnDescriptionChanged(val newValue: String) : Event()
        data class DeadlineSwitchChecked(val newValue: Boolean) : Event()
        data object DeleteIconClicked : Event()
        data object SaveClicked : Event()
        data class NewDeadlineDateSelected(val newDate: LocalDate) : Event()
        data object SelectDeadlineDateClicked : Event()
        data object SelectDeadlineDialogDismissed : Event()
        data object ExpandPriorityDropdown : Event()
        data object CollapsePriorityDropdown : Event()
        data class NewPrioritySelected(val newPriority: Priority) : Event()
        data object CloseClicked : Event()
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

            Event.CloseClicked -> onEffect(Effect.GoBack)

            Event.SelectDeadlineDialogDismissed -> _state.update {
                it.copy(showSelectDeadlineDateDialog = false)
            }
        }
    }

    private fun onSave() {
        if (isSavingOrDeleting) return
        isSavingOrDeleting = true

        val validationResult =
            validateDescriptionUseCase(description = state.value.description)

        when (validationResult.errorOrNull()) {
            ValidateDescriptionUseCase.DescriptionError.Empty -> {
                onEffect(Effect.ShowMessage(
                    UiText.StringResource(R.string.description_cannot_be_empty)
                ))
                isSavingOrDeleting = false
                return
            }
            null -> Unit
        }

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
        val newItem = todoItem.copy(
            description = state.description,
            deadline = deadline,
            modified = modified,
            priority = state.priority,
        )
        editTodoItemUseCase(newItem)
    }

    private suspend fun saveNewTodoItem(state: State) {
        val deadline: LocalDate? = if (state.deadLineSwitchIsOn) state.deadlineDate else null
        val uuid = uuidGenerator.generateUuid()
        val created = LocalDate.now()
        val modified = created
        val newItem = TodoItem(
            id = uuid,
            description = state.description,
            priority = state.priority,
            created = created,
            deadline = deadline,
            isCompleted = false,
            modified = modified,
            lastUpdatedBy = deviceIdManager.getDeviceId(),
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

    private fun onNewDate(newDate: LocalDate) {
        val isBeforeToday = newDate < LocalDate.now()
        if (isBeforeToday) {
            onEffect(Effect.ShowToast(UiText.StringResource(R.string.invalid_deadline_date)))
            return
        }
        _state.update {
            it.copy(
                deadlineDate = newDate,
                deadlineStringDate = newDate.toLocalizedDate(),
                showSelectDeadlineDateDialog = false,
            )
        }
    }

    private fun getDefaultDeadlineDate(): LocalDate {
        return LocalDate.now().plus(1, DateTimeUnit.DAY)
    }

}
