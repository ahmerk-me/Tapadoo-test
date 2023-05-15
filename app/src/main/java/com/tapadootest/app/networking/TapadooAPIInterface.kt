package com.tapadootest.app.networking

import com.tapadootest.app.models.user.GetUser
import com.tapadootest.app.models.user.RegisterRequest
import com.tapadootest.app.models.ResponseMessage
import com.google.gson.JsonObject
import com.tapadootest.app.models.books.BookItem
import com.tapadootest.app.models.books.GetBooks
import retrofit2.Call
import retrofit2.http.*

interface TapadooAPIInterface {

    @GET("books")
    fun getBooks(): Call<GetBooks?>

    @GET("book/{id}")
    fun getBookById(@Path("id") id: Int): Call<BookItem?>

    @POST("Register")
    fun register(@Body registerRequest: RegisterRequest): Call<ResponseMessage>

    @POST("UpdateProfile")
    fun editProfile(@Body getUser: GetUser): Call<ResponseMessage>

    @GET("MyProfile")
    fun myProfile(@Query("userId") userId: String?): Call<GetUser?>?

    @POST("ForgotPassword")
    fun forgotPassword(@Query("email") token: String?): Call<ResponseMessage?>?

    @GET("Logout")
    fun logout(
        @Query("deviceType") deviceType: Int?,
        @Query("token") token: String?
    ): Call<ResponseMessage?>?

    @POST("UpdateDeviceToken")
    fun updateDeviceToken(@Body request: JsonObject?): Call<ResponseMessage?>?

    @POST("Login")
    fun login(@Body body: JsonObject): Call<ResponseMessage>
}