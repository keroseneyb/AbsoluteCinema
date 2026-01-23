package com.kerosene.absolutecinema.presentation.screens.details

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kerosene.absolutecinema.R
import com.kerosene.absolutecinema.domain.entity.Review
import com.kerosene.absolutecinema.domain.entity.ReviewType
import com.kerosene.absolutecinema.domain.entity.Trailer
import com.kerosene.absolutecinema.presentation.screens.details.model.MovieDetailsUiModel

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel = hiltViewModel(),
    movieId: Int
) {
    val uiState by viewModel.state.collectAsState()

    MovieDetailsScreenContent(
        movieId = movieId,
        uiState = uiState,
        loadMovie = { movieId ->
            viewModel.loadMovie(movieId)
        },
        onToggleFavourite = { movieId, title ->
            viewModel.toggleFavourite(movieId, title)
        },
        onTrailerClick = { url ->
            viewModel.openTrailer(url)
        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MovieDetailsScreenContent(
    movieId: Int,
    uiState: MovieDetailsUiState,
    loadMovie: (Int) -> Unit,
    onToggleFavourite: (Int, String) -> Unit,
    onTrailerClick: (String) -> Unit
) {
    LaunchedEffect(movieId) { loadMovie(movieId) }

    when (uiState) {
        is MovieDetailsUiState.Loading -> ShowLoading()
        is MovieDetailsUiState.Error -> ShowError(uiState.message)
        is MovieDetailsUiState.Success -> ShowMovieDetails(
            isFavourite = uiState.isFavourite,
            onToggleFavourite = onToggleFavourite,
            onTrailerClick = onTrailerClick,
            movie = uiState.movieDetails
        )
    }
}

@Composable
private fun ShowLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ShowError(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(R.string.error_details, message))
    }
}

@Composable
private fun ShowMovieDetails(
    isFavourite: Boolean,
    onToggleFavourite: (Int, String) -> Unit,
    onTrailerClick: (String) -> Unit,
    movie: MovieDetailsUiModel,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            MoviePoster(
                movie = movie,
                isFavourite = isFavourite,
                onToggleFavourite = onToggleFavourite
            )
        }

        item {
            MovieInfoCard(movie = movie)
        }

        if (movie.trailers.isNotEmpty()) {
            item { SectionHeader(stringResource(R.string.trailers)) }
            items(items = movie.trailers) { TrailerItem(trailer = it, onTrailerClick = onTrailerClick) }
        }

        if (movie.reviews.isNotEmpty()) {
            item { SectionHeader(stringResource(R.string.reviews)) }
            items(items = movie.reviews) { ReviewItem(review = it) }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MoviePoster(
    movie: MovieDetailsUiModel,
    isFavourite: Boolean,
    onToggleFavourite: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        GlideImage(
            model = movie.details.poster?.url,
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
            onClick = { onToggleFavourite(movie.details.id, movie.details.name ?: "") },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(Color.White.copy(alpha = 0.8f), CircleShape)
        ) {
            Icon(
                imageVector = if (isFavourite) {
                    Icons.Default.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = null,
                tint = if (isFavourite) {
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
    movie: MovieDetailsUiModel,
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
                movie.details.name ?: "",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.rating, movie.details.rating.kp),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                stringResource(R.string.year, movie.details.year),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(
                    R.string.genre,
                    movie.details.genre?.joinToString { it.name } ?: "emptyList()"),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(
                    R.string.country,
                    movie.details.country.joinToString { it.name }),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(R.string.description),
                style = MaterialTheme.typography.titleSmall
            )
            movie.details.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun TrailerItem(
    trailer: Trailer,
    onTrailerClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onTrailerClick(trailer.url) },
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
private fun ReviewItem(review: Review) {
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