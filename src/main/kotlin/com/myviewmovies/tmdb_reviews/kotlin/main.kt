package com.myviewmovies.tmdb_reviews.kotlin

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val REQUEST_LIMIT = 40
const val MOVIES_PER_PAGE = 20
const val PAGES_TO_FETCH = 50

fun main() {
    //fetch popular movies and store them to DataList
    storeListOfPopularMovies(PAGES_TO_FETCH)

    print("Finished fetching movies! \n\n")

    //print movies in DataList
    for(movie in 0..(DataList.movieList.size - 1)) {
        print("\nFor Movie ${movie} \n")

        addReviewsForMovieToDataList(DataList.movieList[movie].id) //get reviews for the movies on the DataList

        //because of 40 calls per 10 second limit, sleep the thread 11 seconds to ensure no queries are made that could fail
        if (movie % 40 == 0) Thread.sleep(11000)
    }
}

//adds a movie review to the DataList object given a movie review id
fun addReviewsForMovieToDataList(movie_id: Int) : ReviewsForMovieData? {
    var tempReviewsForMovieData: ReviewsForMovieData? = null

    //query for a review
    TmdbApi.retrofitService.searchMovieReviews(movie_id).enqueue(object : Callback<ReviewsForMovieData> {

        override fun onFailure(p0: Call<ReviewsForMovieData>, p1: Throwable) {
            print("Did not get reviewList for movie ${movie_id}\n")
        }

        override fun onResponse(call: Call<ReviewsForMovieData>, response: Response<ReviewsForMovieData>) {
            print ("Fetched movie reviews for id: $movie_id\n")
            tempReviewsForMovieData = response.body()
            print("Fetched ${tempReviewsForMovieData?.results?.size} review results \n")
            if (tempReviewsForMovieData != null) {
                print("Grabbed reviews successfully!\n")

                //add each review to the ReviewList in the DataList object
                for (review in tempReviewsForMovieData!!.results) {
                    DataList.add(review)
                    print("Added review from ${review.author} at ${review.url} \n")
                    print("Collected a total of ${DataList.reviewList.size} reviews so far! \n")
                }
            }
        }
    })
    return tempReviewsForMovieData
}

//using the MovieList in the DataList object, find all the reviews ids for every movie,
//      then call addReviewsForMovieToDataList to fetch the full review
fun getReviewsForPopularMovies(page: Int) : PopularMoviesPageData? {
    var tempPopularMoviesPageData: PopularMoviesPageData? = null
    var tempMovie: MovieData? = null
    var tempReviewsForMovieData: ReviewsForMovieData? = null
    TmdbApi.retrofitService.getPopularMovies(page).enqueue(object: Callback<PopularMoviesPageData> {
        override fun onFailure(call: Call<PopularMoviesPageData>, t: Throwable) {
            print("Did not get discover reviewList because of error: ${t.message}\n")
        }

        override fun onResponse(call: Call<PopularMoviesPageData>, response: Response<PopularMoviesPageData>) {
            tempPopularMoviesPageData = response.body()
            if (tempPopularMoviesPageData != null) {
                print("Fetched popular movies. \n")

                for (movie in 0..(tempPopularMoviesPageData!!.results.size - 1)) { //fetches reviews for 20 movies
                    tempMovie = tempPopularMoviesPageData!!.results[movie]
                    if (tempMovie != null) {
                        print("Started grabbing reviews for movie: ${tempMovie?.title}\n")
                        addReviewsForMovieToDataList(tempMovie!!.id)
                    }
                }
            }
        }
    })
    return tempPopularMoviesPageData
}

//stores most popular movies on the TMDB API (quantity: 20 movies * pagesToFetch)
fun storeListOfPopularMovies(pagesToFetch: Int) {
    var counter = 0
    for (page in 1..pagesToFetch) {
        TmdbApi.retrofitService.getPopularMovies(page).enqueue(object: Callback<PopularMoviesPageData> {
            override fun onFailure(call: Call<PopularMoviesPageData>, t: Throwable) {
                print(call.request().url().toString() + " resulted in error: ${t}")
            }

            //store the movies for each page to the MovieList in the DataList object
            override fun onResponse(call: Call<PopularMoviesPageData>, response: Response<PopularMoviesPageData>) {
                if (response.body()?.results != null) {
                    for (movie in response.body()?.results!!) {
                        print("Stored ${movie.title} \n")
                        DataList.addMovie(movie)
                        print("Stored a total of ${DataList.movieList.size} movies \n")
                    }
                }
                else print(response.toString() + '\n')
            }
        })
        counter++
        if (counter % 40 == 0 || page == pagesToFetch) { //limit calls to 40 every 11 secs due to limited query numbers
            Thread.sleep(11000)
        }
    }
}
