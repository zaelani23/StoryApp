package com.segalahal.dicodingstoryapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.segalahal.dicodingstoryapp.data.remote.response.StoryItem
import com.segalahal.dicodingstoryapp.network.ApiService

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, StoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllStories("", page, params.loadSize)

            LoadResult.Page(
                data = responseData.listStory!!,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}