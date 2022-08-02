package com.segalahal.dicodingstoryapp.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.segalahal.dicodingstoryapp.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AppDataSource {
    fun login(email: String, password: String): LiveData<LoginResponse>
    fun register(name: String, email: String, password: String): LiveData<RegisterResponse>
    fun getUser(): LiveData<LoginResult>
    fun deleteUser()
    fun getStories(bearer: String): LiveData<PagingData<StoryItem>>
    fun getStoryMaps(bearer: String): LiveData<StoriesResponse>
    fun uploadFile(bearer: String, file: MultipartBody.Part, description: RequestBody):
            LiveData<StoryUploadResponse>
}