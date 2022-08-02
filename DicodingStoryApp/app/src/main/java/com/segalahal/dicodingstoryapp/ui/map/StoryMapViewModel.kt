package com.segalahal.dicodingstoryapp.ui.map

import androidx.lifecycle.ViewModel
import com.segalahal.dicodingstoryapp.data.Repository

class StoryMapViewModel(private val repository: Repository) : ViewModel() {
    fun getStoryMaps(bearer: String) = repository.getStoryMaps(bearer)
}