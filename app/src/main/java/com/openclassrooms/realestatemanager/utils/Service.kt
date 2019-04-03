package com.openclassrooms.realestatemanager.utils

import com.openclassrooms.realestatemanager.models.geocoding.ResultGeocoding
import com.openclassrooms.realestatemanager.utils.Service.Companion.TOKEN
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface Service {

    // GEOCODING
    @GET("geocode/json")
    fun getGeocoding(@Query(ADDRESS) address: String, @Query("key") token: String = TOKEN): Observable<ResultGeocoding>

    // ----------------------------------------------

    companion object {

        const val TOKEN = "AIzaSyCPwec8XpQW3rbXeT9-1b15ibSiGLcrlPM"
        private const val BASE_URL = "https://maps.googleapis.com/maps/api/"
        private const val ADDRESS = "address"

        val retrofitS = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }
}

val okHttp = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
        .addInterceptor {
            val request = it.request().newBuilder()
                    .addHeader("key", TOKEN)
                    .build()
            it.proceed(request)
        }
        .callTimeout(20, TimeUnit.SECONDS)
        .build()