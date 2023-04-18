package com.example.composerecipeapp.domain.util

sealed class Resource <out T>(val data: T? = null, val error: String? = null)
{
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(error: String? = null, data: T? = null): Resource<T>(data, error)
    object Loading: Resource<Nothing>()
}

sealed interface LoginResult
{
    object Success : LoginResult
    class Failed(val error: String): LoginResult
}

sealed interface UiEvent
{
    object Success: UiEvent
    object NavigateUp: UiEvent
    data class NavigateTo(val route: String, val passedId: Int? = null): UiEvent
    data class showSnackBar(val msg: String): UiEvent
}
