package com.kerosene.absolutecinema.data.network.api.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kerosene.absolutecinema.data.network.api.ApiService
import com.kerosene.absolutecinema.data.network.dto.MovieDto

class AllMoviesPagingSource(
    private val apiService: ApiService,
) : PagingSource<Int, MovieDto>() {

    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        return try {
            val page = params.key ?: 1
            val limit = 30
            val response = apiService.loadPagingAllMovies(page = page, limit = limit).movies

            val nextKey = if (response.isEmpty()) null else response.size.plus(page).plus(1)
            val prevKey = if (page == 1) null else response.size.minus(limit)

            LoadResult.Page(
                data = response,
                nextKey = nextKey,
                prevKey = prevKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}