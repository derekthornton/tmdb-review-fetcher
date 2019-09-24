package com.myviewmovies.tmdb_reviews.kotlin

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val API_KEY_3 = "dff03da12252d6d53d0b3ccf788dc612"

//create a Json to Kotlin Adapter
val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//create an OkHttpClient for API access use
val httpClient = OkHttpClient.Builder()

//create an instance of retrofit to handle API calls
val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(httpClient.build())
    .baseUrl(BASE_URL)
    .build()

//define calls to the TmdpApiService
interface TmdbApiService {
    @GET("movie/{movie_id}/reviews")
    fun searchMovieReviews(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = API_KEY_3) :
            Call<ReviewsForMovieData>

    @GET("discover/movie")
    fun getPopularMovies(
        @Query("page") page: Int,
        @Query("api_key") api_key: String = API_KEY_3,
        @Query("language") language: String = "en-US",
        @Query("sort_by") sort_by: String = "vote_count.desc") :
            Call<PopularMoviesPageData>
}

object TmdbApi {
    val retrofitService: TmdbApiService by lazy {
        retrofit.create(TmdbApiService::class.java)
    }
}