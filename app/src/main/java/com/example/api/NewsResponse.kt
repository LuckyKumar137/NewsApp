package com.example.api

data class NewsResponse(

    var status : String,
    var totalResults : String,
    var articles : List<NewsData>
)
