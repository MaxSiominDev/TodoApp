package dev.maxsiomin.todoapp.feature.todolist.data.remote

internal object HttpRoutes {

    private const val BASE_URL = "https://hive.mrdekk.ru/todo"

    fun getTodoItemsListRoute() = "$BASE_URL/list"

    fun updateTodoItemsListRoute() = "$BASE_URL/list"

    fun getTodoItemByIdRoute(id: String) = "$BASE_URL/list/$id"

    fun addTodoItemRoute() = "$BASE_URL/list"

    fun changeTodoItemByIdRoute(id: String) = "$BASE_URL/list/$id"

    fun deleteTodoItemByIdRoute(id: String) = "$BASE_URL/list/$id"

}