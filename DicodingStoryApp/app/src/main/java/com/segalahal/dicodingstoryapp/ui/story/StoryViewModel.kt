package com.segalahal.dicodingstoryapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.segalahal.dicodingstoryapp.data.Repository
import com.segalahal.dicodingstoryapp.data.remote.response.StoryItem

class StoryViewModel(private val repository: Repository) : ViewModel() {
    fun getStories(bearer: String) : LiveData<PagingData<StoryItem>> =
        repository.getStories(bearer).cachedIn(viewModelScope)
    fun deleteUser() = repository.deleteUser()
}