package com.kerosene.absolutecinema.data.network.api

import com.kerosene.absolutecinema.data.network.dto.MovieDto
import com.kerosene.absolutecinema.data.network.dto.MovieResponse
import com.kerosene.absolutecinema.data.network.dto.ReviewResponse
import com.kerosene.absolutecinema.data.network.dto.TrailerResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie?lists=popular-films&selectFields=&rating.kp=1-10")
    suspend fun loadPopularMovies(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 30,
    ): MovieResponse

    @GET("movie/{id}")
    suspend fun loadMovieDetails(
        @Path("id") id: Int,
    ): MovieDto

    @GET("movie/search")
    suspend fun searchMovie(
        @Query("query") query: String,
    ): MovieResponse

    @GET("movie?selectField=rating.kp&search=7-10&sortField=votes.kp&sortType=-1")
    suspend fun loadAllMovies(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 30,
    ): MovieResponse

    @GET("movie?selectFields=videos")
    suspend fun loadTrailers(
        @Query("id") id: Int,
    ): TrailerResponse

    @GET("review")
    suspend fun loadReviews(
        @Query("movieId") movieId: Int,
    ): ReviewResponse
}