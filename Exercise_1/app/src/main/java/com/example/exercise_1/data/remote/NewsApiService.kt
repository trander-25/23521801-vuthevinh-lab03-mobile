package com.example.exercise_1.data.remote

import com.example.exercise_1.data.model.NewsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/everything")
    suspend fun getEverything(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse

    companion object {
        private const val BASE_URL = "https://newsapi.org/"
        private const val API_KEY = "a64b139b69e3489fb8f48abe4910d96d"

        fun create(): NewsApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApiService::class.java)
        }
    }
}

