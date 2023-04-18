package com.example.composerecipeapp.presentation.util

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun RATextField(
        modifier: Modifier = Modifier,
        onValueChanged: (String) -> Unit,
        strLabel: String,
        strPlaceHolder: String,
        value: String,
        visualTransformation: VisualTransformation? = null
)
{
    TextField(
            modifier = modifier,
            label = { Text(text = strLabel, color = MaterialTheme.colors.primaryVariant) },
            placeholder = {
                Text(text = strPlaceHolder,
                        style = MaterialTheme.typography.caption,
                        color = Color.LightGray)
            },
            value = value,
            onValueChange = onValueChanged,
            visualTransformation = visualTransformation ?: VisualTransformation.None,
            textStyle = MaterialTheme.typography.body1
    )
}