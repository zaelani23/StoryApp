package com.segalahal.dicodingstoryapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.segalahal.dicodingstoryapp.data.local.UserPreferences
import com.segalahal.dicodingstoryapp.data.local.database.StoryDatabase
import com.segalahal.dicodingstoryapp.data.remote.RemoteDataSource
import com.segalahal.dicodingstoryapp.data.remote.response.*
import com.segalahal.dicodingstoryapp.network.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val userPreferences: UserPreferences,
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) : AppDataSource{

    override fun login(email: String, password: String): LiveData<LoginResponse> {
        val loginResponse = MutableLiveData<LoginResponse>()

        remoteDataSource.login(object : RemoteDataSource.LoginCallback{
            override fun onLogin(login: LoginResponse) {
                loginResponse.postValue(login)
                if (!login.error){
                    login.loginResult?.let { userPreferences.setUser(it) }
                }
            }
        }, email, password)
        return loginResponse
    }

    override fun register(name: String, email: String, password: String): LiveData<RegisterResponse> {
        val registerResponse = MutableLiveData<RegisterResponse>()

        remoteDataSource.register(object : RemoteDataSource.RegisterCallback{
            override fun onRegister(register: RegisterResponse) {
                registerResponse.postValue(register)
            }
        }, name, email, password)
        return registerResponse
    }

    override fun getUser(): LiveData<LoginResult> {
        val user = MutableLiveData<LoginResult>()
        user.postValue(userPreferences.getUser())
        return user
    }

    override fun deleteUser() {
        userPreferences.deleteUser()
    }

    override fun getStories(bearer: String): LiveData<PagingData<StoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, bearer),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    override fun getStoryMaps(bearer: String): LiveData<StoriesResponse> {
        val storiesResponse = MutableLiveData<StoriesResponse>()

        remoteDataSource.getStoryMaps(object : RemoteDataSource.StoryMapCallback{
            override fun onStoryMapLoaded(stories: StoriesResponse) {
                storiesResponse.postValue(stories)
            }
        }, bearer)
        return storiesResponse
    }


    override fun uploadFile(
        bearer: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<StoryUploadResponse> {
        val uploadResponseStatus = MutableLiveData<StoryUploadResponse>()

        remoteDataSource.uploadFile(object : RemoteDataSource.UploadCallback{
            override fun onUploaded(uploadResponse: StoryUploadResponse) {
                uploadResponseStatus.postValue(uploadResponse)
            }
        }, bearer, file, description)
        return uploadResponseStatus
    }

    companion object {
        @Volatile
        private var instance: Repository? = null

        @JvmStatic
        fun getInstance(
            remoteDataSource: RemoteDataSource,
            pref: UserPreferences,
            storyDatabase: StoryDatabase,
            apiService: ApiService
        ) : Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(remoteDataSource, pref, storyDatabase, apiService)
            }.also { instance = it }
    }
}