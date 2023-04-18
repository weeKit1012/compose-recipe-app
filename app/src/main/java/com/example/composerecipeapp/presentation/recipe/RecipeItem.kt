package com.example.composerecipeapp.presentation.recipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.composerecipeapp.R
import com.example.composerecipeapp.domain.model.Recipe

@Composable
fun RecipeItem(
        item: Recipe,
        modifier: Modifier
)
{
    Row(modifier = modifier
        .height(120.dp)
        .border(2.dp, Color.LightGray, RoundedCornerShape(10))
        .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
                painter = rememberImagePainter(
                        data = item.imageUri,
                        builder = {
                            crossfade(true)
                            error(R.drawable.ic_lunch)
                            fallback(R.drawable.ic_lunch)
                        }
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.LightGray, CircleShape)
        )

        Column(modifier = Modifier
            .padding(start = 20.dp)
            .weight(1f)
        ) {
            Text(text = item.title, style = MaterialTheme.typography.body1, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = item.description, style = MaterialTheme.typography.body2, color = Color.LightGray, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
}