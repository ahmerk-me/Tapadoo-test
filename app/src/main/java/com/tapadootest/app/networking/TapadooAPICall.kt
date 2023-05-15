package com.cells.library.networking

import com.tapadootest.app.classes.LanguageSessionManager
import com.tapadootest.app.classes.SessionManager
import com.tapadootest.app.classes.BASE_URL
import com.tapadootest.app.classes.getCurrentTimezoneOffset
import com.tapadootest.app.classes.sessionManager
import com.tapadootest.app.networking.TapadooAPIInterface

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class TapadooAPICall {

    companion object {

        lateinit var retrofit: Retrofit

        private fun getClient(sessionManager: SessionManager?, isFirst: Boolean): Retrofit {

            var interceptor = HttpLoggingInterceptor()

            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .addInterceptor(object : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): Response {
                        var newRequest = chain.request().newBuilder()
//                            .addHeader("Accept-Language", LanguageSessionManager.language)
                            .addHeader("Content-Type", "application/json")
//                            .addHeader("Authorization", if (!sessionManager!!.isLoggedin) "" else sessionManager?.token ?: "")
//                            .addHeader("OffsetHours", getCurrentTimezoneOffset() +"")
//                            .addHeader("OffsetHours", "3")
                            .build()
                        return chain.proceed(newRequest)
                    }
                })
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .addInterceptor(interceptor)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit
        }

        fun apiInterface(): TapadooAPIInterface? {
            return getClient(sessionManager, false).create(TapadooAPIInterface::class.java)
        }

        fun apiInterfaceFirstTime(): TapadooAPIInterface? {
            return getClient(sessionManager,true).create(TapadooAPIInterface::class.java)
        }
    }
}