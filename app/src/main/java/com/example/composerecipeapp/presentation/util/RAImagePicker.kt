package com.example.composerecipeapp.presentation.util

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.composerecipeapp.R

@ExperimentalCoilApi
@Composable
fun RAImagePicker(
        modifier: Modifier,
        onSelectImageUri: (Uri?) -> Unit,
        currentImageUri: Uri? = null,
        isClickToSelectEnabled: Boolean = false
)
{
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        onSelectImageUri(it)
    }

    Column(modifier = modifier
        .clickable {
            if (isClickToSelectEnabled)
            {
                launcher.launch("image/*")
            }
        }
        .border(2.dp, Color.LightGray),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (currentImageUri != null)
        {
            AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(currentImageUri)
                        .crossfade(true)
                        .fallback(R.drawable.ic_lunch)
                        .error(R.drawable.ic_lunch)
                        .build(),
                    contentDescription = null
            )
        }
        else
        {
            Image(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(40.dp))
        }
    }
}