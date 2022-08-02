package com.segalahal.dicodingstoryapp.di

import android.content.Context
import com.segalahal.dicodingstoryapp.data.remote.RemoteDataSource
import com.segalahal.dicodingstoryapp.data.Repository
import com.segalahal.dicodingstoryapp.data.local.UserPreferences
import com.segalahal.dicodingstoryapp.data.local.database.StoryDatabase
import com.segalahal.dicodingstoryapp.network.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository{
        val userPreferences = UserPreferences.getInstance(context)
        val remoteDataSource = RemoteDataSource.getInstance()
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(remoteDataSource, userPreferences, database, apiService)
    }
}