package com.example.jobfinder.API

import android.content.Context
import com.example.jobfinder.Services.AuthRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "http://192.168.11.13:8081/"

    private lateinit var authRepository: AuthRepository

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // Timeout de conexão
            .readTimeout(60, TimeUnit.SECONDS)    // Timeout de leitura
            .writeTimeout(60, TimeUnit.SECONDS)   // Timeout de escrita
            .addInterceptor { chain ->
                val original: Request = chain.request()
                val token = authRepository.getToken()
                val requestBuilder: Request.Builder = original.newBuilder()
                if (!token.isNullOrBlank()) {
                    requestBuilder.header("Authorization", "Bearer $token")
                }
                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Inicializa o RetrofitClient com o contexto (obrigatório chamar no início)
    fun init(context: Context) {
        authRepository = AuthRepository(context)
    }

    // Expõe o serviço da API
    val apiService: ApiService by lazy {
        if (!::authRepository.isInitialized) {
            throw IllegalStateException("RetrofitClient não foi inicializado. Chame RetrofitClient.init(context) primeiro.")
        }
        retrofit.create(ApiService::class.java)
    }
}
