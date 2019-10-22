package com.vferreirati.tormovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.vferreirati.tormovies.network.services.MoviesService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
        val service = Retrofit.Builder()
            .baseUrl("https://tv-v2.api-fetch.website/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(MoviesService::class.java)

        GlobalScope.launch {
            try {
                val response = service.queryMovies()
                for(entry in response) {
                    Log.d("TOR", entry.title)
                }

            } catch (e: Throwable) {
                Log.e("TOR", "Error: ", e)
            }
        }
    }
}
