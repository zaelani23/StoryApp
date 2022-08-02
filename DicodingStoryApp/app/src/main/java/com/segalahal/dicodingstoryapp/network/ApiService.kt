package com.segalahal.dicodingstoryapp.network

import com.segalahal.dicodingstoryapp.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("/v1/stories")
    fun uploadStory(
        @Header("Authorization") bearer: String?,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody?,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): Call<StoryUploadResponse>

    @FormUrlEncoded
    @POST("/v1/register")
    fun register(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("/v1/login")
    fun login(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<LoginResponse>

    @GET("/v1/stories")
    suspend fun getAllStories(
        @Header("Authorization") bearer: String?,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        ): StoriesResponse

    @GET("/v1/stories")
    fun getMaps(
        @Header("Authorization") bearer: String?,
        @Query("location") page: Int = 1,
    ): Call<StoriesResponse>
}