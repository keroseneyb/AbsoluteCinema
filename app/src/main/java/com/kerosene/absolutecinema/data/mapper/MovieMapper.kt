package com.kerosene.absolutecinema.data.mapper

import com.kerosene.absolutecinema.data.local.model.MovieDbModel
import com.kerosene.absolutecinema.data.network.dto.CountryDto
import com.kerosene.absolutecinema.data.network.dto.GenreDto
import com.kerosene.absolutecinema.data.network.dto.MovieDto
import com.kerosene.absolutecinema.data.network.dto.MovieTrailersDto
import com.kerosene.absolutecinema.data.network.dto.PosterDto
import com.kerosene.absolutecinema.data.network.dto.RatingDto
import com.kerosene.absolutecinema.data.network.dto.ReviewDto
import com.kerosene.absolutecinema.domain.entity.Country
import com.kerosene.absolutecinema.domain.entity.Genre
import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.entity.Poster
import com.kerosene.absolutecinema.domain.entity.Rating
import com.kerosene.absolutecinema.domain.entity.Review
import com.kerosene.absolutecinema.domain.entity.ReviewType
import com.kerosene.absolutecinema.domain.entity.Trailer

fun MovieDto.toMovie(): Movie = Movie(
    id = id,
    name = name,
    rating = ratingDto.toRating(),
    year = year,
    country = countriesDto.map { it.toCountry() },
    genre = genresDto.map { it.toGenre() },
    description = description,
    shortDescription = shortDescription,
    poster = posterDto?.toPoster()
)

fun RatingDto.toRating(): Rating = Rating(kp = kp)
fun CountryDto.toCountry(): Country = Country(name = name)
fun PosterDto.toPoster(): Poster = Poster(url = url)
fun GenreDto.toGenre(): Genre = Genre(name = name)

fun MovieTrailersDto.toTrailer(): Trailer {
    val dto = videosContainer.trailers.first()
    return Trailer(
        url = dto.trailerUrl,
        name = dto.name
    )
}

fun ReviewDto.toReview(): Review = Review(
    author = author,
    title = title,
    review = review,
    type = ReviewType.fromValue(type)
)

fun Movie.toDbModel(): MovieDbModel = MovieDbModel(
    id = id,
    name = name ?: "",
    year = year,
    description = description,
    shortDescription = shortDescription,
    posterUrl = poster?.url,
    ratingKp = rating.kp,
    countries = country.map { it.name },
    genres = genre.map { it.name }
)

fun MovieDbModel.toMovieEntity(): Movie = Movie(
    id = id,
    name = name,
    year = year,
    description = description ?: "",
    shortDescription = shortDescription ?: "",
    poster = Poster(url = posterUrl),
    rating = Rating(ratingKp),
    country = countries.map { Country(it) },
    genre = genres.map { Genre(it) }
)

fun List<MovieDbModel>.toMovieList(): List<Movie> = map { it.toMovieEntity() }


