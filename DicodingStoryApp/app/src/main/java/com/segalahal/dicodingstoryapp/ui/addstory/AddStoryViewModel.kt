package com.segalahal.dicodingstoryapp.ui.addstory

import androidx.lifecycle.ViewModel
import com.segalahal.dicodingstoryapp.data.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: Repository) : ViewModel() {
    fun uploadStory(bearer: String, file: MultipartBody.Part, description: RequestBody) =
        repository.uploadFile(bearer, file, description)
}