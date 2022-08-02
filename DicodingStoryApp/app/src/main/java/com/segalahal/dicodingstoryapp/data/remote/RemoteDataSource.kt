package com.segalahal.dicodingstoryapp.data.remote

import com.segalahal.dicodingstoryapp.data.remote.response.*
import com.segalahal.dicodingstoryapp.network.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RemoteDataSource {

    fun login(callback: LoginCallback, email: String, password: String){
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    response.body()?.let { callback.onLogin(it) }
                }else {
                    val loginError = LoginResponse(
                        null,
                        true,
                        "Login gagal!"
                    )
                    callback.onLogin(
                        loginError
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val loginError = LoginResponse(
                    null,
                    true,
                    t.message.toString()
                )
                callback.onLogin(
                    loginError
                )
            }
        })
    }

    fun register(callback: RegisterCallback, name: String, email: String, password: String){
        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if(response.isSuccessful){
                    response.body()?.let { callback.onRegister(it) }
                }else{
                    val registerError = RegisterResponse(
                        true,
                        "Registrasi gagal!"
                    )
                    callback.onRegister(
                        registerError
                    )
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                val registerError = RegisterResponse(
                    true,
                    t.message.toString()
                )
                callback.onRegister(
                    registerError
                )
            }

        })
    }

    fun getStoryMaps(callback: StoryMapCallback, bearer: String){
        val client = ApiConfig.getApiService().getMaps(bearer)

        client.enqueue(object : Callback<StoriesResponse>{
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful){
                    response.body()?.let { callback.onStoryMapLoaded(it) }
                }else{
                    val storiesResponse = StoriesResponse(
                        null,
                        true,
                        "Gagal load lokasi story!"
                    )
                    callback.onStoryMapLoaded(storiesResponse)
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                val storiesResponse = StoriesResponse(
                    null,
                    true,
                    "Gagal load lokasi story!"
                )
                callback.onStoryMapLoaded(storiesResponse)
            }
        })
    }

    fun uploadFile(
        callback: UploadCallback,
        bearer: String,
        file: MultipartBody.Part,
        description: RequestBody
    ){
        val client = ApiConfig.getApiService().uploadStory(bearer, file, description)

        client.enqueue(object : Callback<StoryUploadResponse>{
            override fun onResponse(
                call: Call<StoryUploadResponse>,
                response: Response<StoryUploadResponse>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error){
                        callback.onUploaded(responseBody)
                    }else{
                        callback.onUploaded(
                            uploadResponse = StoryUploadResponse(
                                true,
                                "Gagal upload file"
                            )
                        )
                    }
                }
                else{
                    callback.onUploaded(
                        uploadResponse = StoryUploadResponse(
                            true,
                            "Gagal upload file"
                        )
                    )
                }
            }

            override fun onFailure(call: Call<StoryUploadResponse>, t: Throwable) {
                callback.onUploaded(
                    uploadResponse = StoryUploadResponse(
                        true,
                        "Gagal upload file"
                    )
                )
            }
        })
    }

    interface LoginCallback{
        fun onLogin(loginResponse: LoginResponse)
    }

    interface RegisterCallback{
        fun onRegister(registerResponse: RegisterResponse)
    }

    interface UploadCallback{
        fun onUploaded(uploadResponse: StoryUploadResponse)
    }

    interface StoryMapCallback{
        fun onStoryMapLoaded(storyMaps : StoriesResponse)
    }

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource()
            }
    }
}