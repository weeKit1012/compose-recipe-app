package com.example.composerecipeapp.presentation.launch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composerecipeapp.di.CoroutineIO
import com.example.composerecipeapp.domain.use_case.user.UserUseCases
import com.example.composerecipeapp.domain.util.LoginResult
import com.example.composerecipeapp.domain.util.UiEvent
import com.example.composerecipeapp.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LaunchScreenEvent
{
    data class OnEnterUserName(val name: String) : LaunchScreenEvent
    data class OnEnterUserPin(val pin: String) : LaunchScreenEvent
    data class OnPermissionGranted(val isGranted: Boolean) : LaunchScreenEvent
    object OnClickBtnLogin : LaunchScreenEvent
    object OnClickBtnRegister : LaunchScreenEvent
    object OnSwitchRegisterFlow : LaunchScreenEvent
}

@HiltViewModel
class LaunchViewModel @Inject constructor(
        private val userUseCases: UserUseCases,
        @CoroutineIO private val ioScope: CoroutineScope
) : ViewModel()
{
    /* ===================================================== */
    /* Stored Properties */
    /* ===================================================== */

    var userName by mutableStateOf("")
        private set
    var userPin by mutableStateOf("")
        private set

    var isRegisterFlow by mutableStateOf(false)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    /* ===================================================== */
    /* Public Methods */
    /* ===================================================== */

    fun onEvent(event: LaunchScreenEvent)
    {
        when (event)
        {
            is LaunchScreenEvent.OnEnterUserName ->
            {
                this.userName = event.name
            }
            is LaunchScreenEvent.OnEnterUserPin ->
            {
                this.userPin = event.pin
            }
            is LaunchScreenEvent.OnClickBtnLogin ->
            {
                this.performLogin()
            }
            is LaunchScreenEvent.OnClickBtnRegister ->
            {
                this.performRegister()
            }
            is LaunchScreenEvent.OnSwitchRegisterFlow ->
            {
                this.userName = ""
                this.userPin = ""
                this.isRegisterFlow = !this.isRegisterFlow
            }
            is LaunchScreenEvent.OnPermissionGranted ->
            {
                viewModelScope.launch {
                    if (event.isGranted)
                    {
                        _uiEvent.send(UiEvent.NavigateTo(Route.OVERVIEW_RECIPE))
                    }
                    else
                    {
                        _uiEvent.send(UiEvent.showSnackBar("Should allow permission to access"))
                    }
                }
            }
        }
    }

    /* ===================================================== */
    /* Private Methods */
    /* ===================================================== */

    private fun performLogin()
    {
        ioScope.launch {

            if (userName.isBlank() || userPin.isBlank())
            {
                _uiEvent.send(UiEvent.showSnackBar("All fields must be entered"))
                return@launch
            }

            userUseCases.loginUser(userName, userPin).let { result ->
                when (result)
                {
                    is LoginResult.Success ->
                    {
                        _uiEvent.send(UiEvent.Success)
                    }
                    is LoginResult.Failed ->
                    {
                        _uiEvent.send(UiEvent.showSnackBar(result.error))
                    }
                }
            }
        }
    }

    private fun performRegister()
    {
        viewModelScope.launch {
            userUseCases.registerUser(userName, userPin)
            _uiEvent.send(UiEvent.Success)
        }
    }
}