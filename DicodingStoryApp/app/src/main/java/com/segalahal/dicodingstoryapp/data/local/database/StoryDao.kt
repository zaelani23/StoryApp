package com.segalahal.dicodingstoryapp.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.segalahal.dicodingstoryapp.data.remote.response.StoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoryItem>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, StoryItem>

    @Query("DELETE FROM story WHERE 1")
    suspend fun deleteAll()
}