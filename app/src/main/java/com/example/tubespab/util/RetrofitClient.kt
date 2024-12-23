package com.example.tubespab.util

import com.example.tubespab.service.GroqApiService
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.groq.com/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val originalRequest: Request = chain.request()

            val newRequest: Request = originalRequest.newBuilder()
                .header("Authorization", "Bearer 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")
                .build()

            chain.proceed(newRequest)
        })
        .build()

    val instance: GroqApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(GroqApiService::class.java)
    }
}
