package com.openclassrooms.realestatemanager.utils

import com.openclassrooms.realestatemanager.models.geocoding.ResultGeocoding
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface Service {

    // GEOCODING
    @GET("geocode/json?address={address}&key=$TOKEN")
    fun getGeocoding(@Path(ADDRESS) address: String): Observable<ResultGeocoding>

    // ----------------------------------------------

    companion object {

        private const val TOKEN = "AIzaSyCPwec8XpQW3rbXeT9-1b15ibSiGLcrlPM"
        private const val BASE_URL = "https://maps.googleapis.com/maps/api/"
        private const val ADDRESS = "address"

        private val okHttpS = okHttp.newBuilder()
                .addInterceptor {
                    val request = it.request().newBuilder()
                            .build()
                    it.proceed(request)
                }
                .build()

        val retrofitS = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpS)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

}

val okHttp = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
        .callTimeout(20, TimeUnit.SECONDS)
        .build()
