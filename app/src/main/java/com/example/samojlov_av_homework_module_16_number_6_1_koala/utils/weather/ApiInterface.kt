package com.example.samojlov_av_homework_module_16_number_6_1_koala.utils.weather

import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.weather.CurrentWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET("weather?")
    suspend fun getCurrentWeatherLocation(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String,
        @Query("lang") lang:String,
        @Query("appid") apiKey: String
    ): Response<CurrentWeather>
}