package com.example.composerecipeapp.presentation.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> RASpinner(
        modifier: Modifier = Modifier,
        dropDownModifier: Modifier = Modifier,
        items: List<T>,
        selectedItem: T,
        onItemSelected: (T) -> Unit,
        selectedItemFactory: @Composable (Modifier, T) -> Unit,
        dropdownItemFactory: @Composable (T, Int) -> Unit,
) {
    var expanded: Boolean by remember { mutableStateOf(false) }

    Box(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
        selectedItemFactory(
                Modifier
                    .clickable { expanded = true },
                selectedItem
        )

        DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = dropDownModifier
        ) {
            items.forEachIndexed { index, element ->
                DropdownMenuItem(onClick = {
                    onItemSelected(items[index])
                    expanded = false
                }) {
                    dropdownItemFactory(element, index)
                }
            }
        }
    }
}