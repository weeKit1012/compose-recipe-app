package com.example.composerecipeapp.presentation.launch

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composerecipeapp.domain.util.UiEvent
import com.example.composerecipeapp.presentation.util.RAActionButton
import com.example.composerecipeapp.presentation.util.RARegularButton
import com.example.composerecipeapp.presentation.util.RATextButton
import com.example.composerecipeapp.presentation.util.RATextField
import kotlinx.coroutines.flow.collect

@Composable
fun LaunchScreen(
        viewModel: LaunchViewModel = hiltViewModel(),
        scaffoldState: ScaffoldState,
        onNavigateTo: (String) -> Unit
)
{
    val focusManager = LocalFocusManager.current

    /* ============================================  */
    /* Permission */
    /* ============================================  */

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
        viewModel.onEvent(LaunchScreenEvent.OnPermissionGranted(it))
    }

    /* ============================================  */
    /* Collector */
    /* ============================================  */
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it)
            {
                is UiEvent.Success ->
                {
                    if (viewModel.isRegisterFlow)
                    {
                        scaffoldState.snackbarHostState.showSnackbar("Operation Success", duration = SnackbarDuration.Short)
                    }
                    else
                    {
                        launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
                is UiEvent.NavigateTo ->
                {
                    onNavigateTo(it.route)
                }
                is UiEvent.NavigateUp ->
                {

                }
                is UiEvent.showSnackBar ->
                {
                    scaffoldState.snackbarHostState.showSnackbar(it.msg, duration = SnackbarDuration.Short)
                }
            }
        }
    }

    /* ============================================  */
    /* UI */
    /* ============================================  */
    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
    ) {
        RATextField(
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChanged = {
                    viewModel.onEvent(LaunchScreenEvent.OnEnterUserName(it))
                },
                strLabel = "Username",
                strPlaceHolder = "Enter your username here",
                value = viewModel.userName,
        )
        Spacer(modifier = Modifier.height(20.dp))
        RATextField(
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChanged = {
                    viewModel.onEvent(LaunchScreenEvent.OnEnterUserPin(it))
                },
                strLabel = "PIN",
                strPlaceHolder = "Enter your PIN here",
                value = viewModel.userPin,
                visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(50.dp))
        if (!viewModel.isRegisterFlow)
        {
            RAActionButton(
                    text = "Login",
                    onClick = {
                        viewModel.onEvent(LaunchScreenEvent.OnClickBtnLogin)
                    }
            )
            Spacer(modifier = Modifier.height(10.dp))
            RATextButton(
                    text = "First time?",
                    onClick = {
                        viewModel.onEvent(LaunchScreenEvent.OnSwitchRegisterFlow)
                    }
            )
        }
        else
        {
            RAActionButton(
                    text = "Register",
                    onClick = {
                        viewModel.onEvent(LaunchScreenEvent.OnClickBtnRegister)
                    }
            )
            Spacer(modifier = Modifier.height(10.dp))
            RATextButton(
                    text = "Back to login?",
                    onClick = {
                        viewModel.onEvent(LaunchScreenEvent.OnSwitchRegisterFlow)
                    }
            )
        }
    }
}