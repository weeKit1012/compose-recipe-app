package com.example.composerecipeapp.presentation.util

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RATwoLineText(
        modifier: Modifier = Modifier,
        strTopText: String,
        strBottomText: String
)
{

    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = strTopText, style = MaterialTheme.typography.h4, color = Color.LightGray)
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = strBottomText, style = MaterialTheme.typography.body1)
    }

}