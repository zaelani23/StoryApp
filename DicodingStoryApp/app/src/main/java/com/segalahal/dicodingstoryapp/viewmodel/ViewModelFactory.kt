package com.segalahal.dicodingstoryapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.segalahal.dicodingstoryapp.data.Repository
import com.segalahal.dicodingstoryapp.di.Injection
import com.segalahal.dicodingstoryapp.ui.addstory.AddStoryViewModel
import com.segalahal.dicodingstoryapp.ui.login.LoginViewModel
import com.segalahal.dicodingstoryapp.ui.map.StoryMapViewModel
import com.segalahal.dicodingstoryapp.ui.register.RegisterViewModel
import com.segalahal.dicodingstoryapp.ui.story.StoryViewModel

class ViewModelFactory private constructor(
    private val repository: Repository
    ) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(StoryMapViewModel::class.java) -> {
                StoryMapViewModel(repository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }

    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }
    }
}