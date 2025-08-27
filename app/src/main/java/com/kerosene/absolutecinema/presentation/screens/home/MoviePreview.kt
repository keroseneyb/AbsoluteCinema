package com.kerosene.absolutecinema.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.kerosene.absolutecinema.R
import com.kerosene.absolutecinema.domain.entity.Movie

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MoviePreviewCard(
    movie: Movie,
    onClick: (Movie) -> Unit,
) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(320.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = { onClick(movie) })
    ) {
        GlideImage(
            model = movie.poster?.url,
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            loading = placeholder(R.drawable.placeholder),
            failure = placeholder(R.drawable.error)
        )
        RatingBox(
            rating = movie.rating.kp,
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.TopEnd)
        )
    }
}

@Composable
fun RatingBox(
    rating: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(Color.Black, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.kinopoisk_item),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = String.format("%.1f", rating),
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}