package com.example.composerecipeapp.presentation.util

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@Composable
fun RARegularButton(
        strTitle: String,
        buttonColor: Color,
        onClick: () -> Unit
)
{
    Column( horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
        ) {
            Text(strTitle, color = Color.White, style = MaterialTheme.typography.caption)
        }
    }
}