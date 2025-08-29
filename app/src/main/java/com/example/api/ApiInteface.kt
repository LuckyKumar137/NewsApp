package com.example.api

import retrofit2.Call
import retrofit2.http.GET

interface ApiInteface {

@GET("top-headlines?country=us&category=business&apiKey=970c8a58338f4b4ead84ac255bd4968b")
fun getData () : Call<NewsResponse>


}
