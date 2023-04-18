package com.example.composerecipeapp.presentation.util

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun RANavigationBar(
        modifier: Modifier = Modifier,
        onNavigateBackOnClicked: (() -> Unit)? = null,
        onNavRightOnClicked: (() -> Unit)? = null,
        btnNavRightType: String? = null,
        strTitle: String
)
{
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(start = 20.dp, end = 20.dp, top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {

        val btnNavLeftAlpha = if (onNavigateBackOnClicked != null) 1f else 0f
        val btnNavRightAlpha = if (onNavRightOnClicked != null) 1f else 0f

        val rightImageVector = when (btnNavRightType)
        {
            BtnNavRightType.ADD ->
            {
                Icons.Default.Add
            }

            BtnNavRightType.EDIT ->
            {
                Icons.Default.Edit
            }

            else ->
            {
                Icons.Default.Add
            }
        }

        IconButton(onClick = onNavigateBackOnClicked ?: { /* Do Nothing */ },
                modifier = Modifier.alpha(btnNavLeftAlpha)) {
            Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous page"
            )
        }

        Text(text = strTitle, color = MaterialTheme.colors.primaryVariant, style = MaterialTheme.typography.h3)

        IconButton(onClick = onNavRightOnClicked ?: { /* Do Nothing */ },
                modifier = Modifier.alpha(btnNavRightAlpha)) {
            Icon(
                    imageVector = rightImageVector,
                    contentDescription = "Add page"
            )
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
}

object BtnNavRightType
{
    const val ADD = "add"
    const val EDIT = "edit"
}