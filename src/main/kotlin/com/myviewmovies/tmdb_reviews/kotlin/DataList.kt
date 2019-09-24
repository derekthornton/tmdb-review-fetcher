package com.myviewmovies.tmdb_reviews.kotlin

//define an object "DataList" to hold data (AuthorData, MovieData and ReviewData)
//      in lists (reviewList, movieList, authorList
object DataList {
    val reviewList : MutableList<ReviewData> = mutableListOf()
    val movieList: MutableList<MovieData> = mutableListOf()
    val authorList: MutableList<AuthorData> = mutableListOf()

    fun add(reviewData: ReviewData) {
        var reviewIsDuplicate = false
        for (review in reviewList) {
            if (review == reviewData) {
                reviewIsDuplicate = true
            }
        }
        if (!(reviewIsDuplicate)) {
            reviewList += reviewData
        }
    }

    fun addMovie(movieData: MovieData) {
        movieList += movieData
    }

    fun addAuthor(author: AuthorData) {
        authorList += author
    }
}