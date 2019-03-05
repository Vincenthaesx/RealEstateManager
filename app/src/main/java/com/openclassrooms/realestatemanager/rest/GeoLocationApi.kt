package com.openclassrooms.realestatemanager.rest

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface GeoLocationApi {

//    // SEARCH
//    @GET("groups/{idApps}/search?scope=projects")
//    fun getSearch(@Path(ID_APPS) id: Int?, @Query("search") query: String? ): Observable<List<Applications>>

    @GET("staticmap?center=Paris&zoom=12&size=600x300&maptype=roadmap&markers=color:blue|label:S|40.702147,-74.015794&markers=color:green|label:G|40.711614,-74.012318&markers=color:red|label:C|40.718217,-73.998284&key=%20AIzaSyCPwec8XpQW3rbXeT9-1b15ibSiGLcrlPM")
    fun getStaticMap()

    // ----------------------------------------------

    companion object {

        private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

        private val okHttpS = okHttp.newBuilder()
                .addInterceptor {
                    val request = it.request().newBuilder()
//                            .addHeader("Private-Token", TOKEN)
                            .build()
                    it.proceed(request)
                }
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpS)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

}

object MyNewsStreams {

    // TopStories
//    fun streamFetchTopStories(section: String): Observable<Articles> {
//        val newsService = MyNewsService.retrofit.create(MyNewsService::class.java)
//
//        return newsService.getTopStoriesArticles(section)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .timeout(100, TimeUnit.SECONDS)
//    }
}
