package com.kerosene.absolutecinema.presentation.screens.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kerosene.absolutecinema.R
import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.entity.Review
import com.kerosene.absolutecinema.domain.entity.ReviewType
import com.kerosene.absolutecinema.domain.entity.Trailer
import com.kerosene.absolutecinema.getApplicationComponent

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieDetailsScreen(
    movieId: Int,
) {

    val component = getApplicationComponent()
    val viewModel: MovieDetailsViewModel = viewModel(factory = component.getViewModelFactory())
    val uiState by viewModel.uiState.collectAsState()
    val isFavorite by viewModel.isFavourite.collectAsState()

    LaunchedEffect(movieId) { viewModel.loadMovie(movieId) }

    when (uiState) {
        is MovieDetailsUiState.Initial, is MovieDetailsUiState.Loading -> ShowLoading()

        is MovieDetailsUiState.Error -> {
            val message = (uiState as MovieDetailsUiState.Error).message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.error_details, message))
            }
        }

        is MovieDetailsUiState.Success -> {
            val data = uiState as MovieDetailsUiState.Success
            val movie = data.movie
            val trailers = data.trailers
            val reviews = data.reviews

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    MoviePoster(
                        movie = movie,
                        isFavorite = isFavorite,
                        onToggleFavourite = { viewModel.toggleFavourite(movie) }
                    )
                }

                item {
                    MovieInfoCard(movie = movie)
                }

                if (trailers.isNotEmpty()) {
                    item { SectionHeader(stringResource(R.string.trailers)) }
                    items(trailers) { TrailerItem(trailer = it) }
                }

                if (reviews.isNotEmpty()) {
                    item { SectionHeader(stringResource(R.string.reviews)) }
                    items(reviews) { ReviewItem(review = it) }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MoviePoster(
    movie: Movie,
    isFavorite: Boolean,
    onToggleFavourite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        GlideImage(
            model = movie.poster?.url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
        )
        IconButton(
            onClick = onToggleFavourite,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(Color.White.copy(alpha = 0.8f), CircleShape)
        ) {
            Icon(
                imageVector = if (isFavorite) {
                    Icons.Default.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = null,
                tint = if (isFavorite) {
                    Color.Red
                } else {
                    Color.Gray
                }
            )
        }
    }
}

@Composable
private fun MovieInfoCard(
    movie: Movie,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                movie.name ?: "",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.rating, movie.rating.kp),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                stringResource(R.string.year, movie.year),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(
                    R.string.genre,
                    movie.genre.joinToString { it.name }),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(
                    R.string.country,
                    movie.country.joinToString { it.name }),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(R.string.description),
                style = MaterialTheme.typography.titleSmall
            )
            movie.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun TrailerItem(trailer: Trailer) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailer.url))
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE4E9FB)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = trailer.name,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color.Red
            )
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = review.type.value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = when (review.type) {
                    ReviewType.POSITIVE -> Color.Green
                    ReviewType.NEGATIVE -> Color.Red
                    ReviewType.NEUTRAL -> Color.Gray
                },
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "${review.author} — ${review.title}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )
            Text(
                text = review.review,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun ShowLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}