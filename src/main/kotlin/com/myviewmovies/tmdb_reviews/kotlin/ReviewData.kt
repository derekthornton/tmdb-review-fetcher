package com.myviewmovies.tmdb_reviews.kotlin

data class ReviewsForMovieData(
    val id: Int,
    val page: Int,
    val results: Array<ReviewData>,
    val total_pages: Int,
    val total_results: Int
)

data class ReviewData(
    val id: String,
    val author: String,
    val content: String,
    val url: String
)

data class AuthorData(
    val author: String,
    val reviews: MutableList<ReviewData>
)

data class PopularMoviesPageData(
    val page: Int,
    val total_pages: Int,
    val results: Array<MovieData>
)

data class  MovieData(
    val poster_path: String?,
    val adult: Boolean,
    val overview: String,
    val release_date: String,
    val genre_ids: Array<Int>,
    val id: Int,
    val original_title: String,
    val original_language: String,
    val title: String,
    val backdrop_path: String?,
    val popularity: Double,
    val vote_count: Int,
    val video: Boolean,
    val vote_average: Double
)