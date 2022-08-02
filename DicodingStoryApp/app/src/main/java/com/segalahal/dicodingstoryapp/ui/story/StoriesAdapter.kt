package com.segalahal.dicodingstoryapp.ui.story

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.segalahal.dicodingstoryapp.R
import com.segalahal.dicodingstoryapp.data.remote.response.StoryItem
import com.segalahal.dicodingstoryapp.databinding.ItemStoryBinding
import com.segalahal.dicodingstoryapp.ui.detailstory.DetailStoryActivity
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

class StoriesAdapter :
    PagingDataAdapter<StoryItem, StoriesAdapter.StoriesViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val itemStoryBinding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoriesViewHolder(itemStoryBinding)
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class StoriesViewHolder(private val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(storyItem: StoryItem){
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            sdf.timeZone = TimeZone.getTimeZone("ICT")
            val time = sdf.parse(storyItem.createdAt)?.time
            val prettyTime = PrettyTime(Locale.getDefault())
            val ago = prettyTime.format(time?.let { Date(it) })
            with(binding){
                tvName.text = storyItem.name
                tvCreated.text = ago
                Glide.with(itemView.context)
                    .load(storyItem.photoUrl)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error))
                    .into(imgItemPhoto)
                itemView.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(imgItemPhoto, "photo"),
                            Pair(imgItemPhoto, "name"),
                            Pair(imgItemPhoto, "created_at"),
                        )
                    val i = Intent(itemView.context, DetailStoryActivity::class.java)
                    i.putExtra(DetailStoryActivity.EXTRA_PERSON, storyItem)
                    itemView.context.startActivity(i, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}