package com.example.fetchexercise.data.network

import com.example.fetchexercise.data.model.HiringModel
import retrofit2.Retrofit
import retrofit2.http.GET

interface ApiService {

    @GET("/hiring.json")
    suspend fun getHiringList(): List<HiringModel>

    companion object Factory {
        fun create(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
    }

}