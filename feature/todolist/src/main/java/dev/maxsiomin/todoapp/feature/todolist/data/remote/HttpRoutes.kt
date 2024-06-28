package dev.maxsiomin.todoapp.feature.todolist.data.remote

object HttpRoutes {

    private const val BASE_URL = "http://beta.mrdekk.ru/todo"
    fun getTodoItemsListRoute() = "$BASE_URL/list"

}