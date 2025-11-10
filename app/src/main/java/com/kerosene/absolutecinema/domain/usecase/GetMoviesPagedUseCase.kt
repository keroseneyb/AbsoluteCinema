package com.kerosene.absolutecinema.domain.usecase

import androidx.paging.PagingData
import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesPagedUseCase @Inject constructor(
    private val repository: MovieRepository,
) {

    operator fun invoke(): Flow<PagingData<Movie>> = repository.getMoviesPaged()
}