package com.segalahal.dicodingstoryapp.ui.detailstory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.segalahal.dicodingstoryapp.R
import com.segalahal.dicodingstoryapp.data.remote.response.StoryItem
import com.segalahal.dicodingstoryapp.databinding.ActivityDetailStoryBinding
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

class DetailStoryActivity : AppCompatActivity() {
    private var _binding : ActivityDetailStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.title_detail_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val story = intent.getParcelableExtra<StoryItem>(EXTRA_PERSON) as StoryItem
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        sdf.timeZone = TimeZone.getTimeZone("ICT")
        val time = sdf.parse(story.createdAt)?.time
        val prettyTime = PrettyTime(Locale.getDefault())
        val ago = prettyTime.format(time?.let { Date(it) })
        with(binding){
            Glide.with(applicationContext)
                .load(story.photoUrl)
                .into(imgStory)
            tvStoryName.text = story.name
            tvCreatedAt.text = ago
            tvDesc.text = story.description
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_PERSON = "extra_person"
    }
}