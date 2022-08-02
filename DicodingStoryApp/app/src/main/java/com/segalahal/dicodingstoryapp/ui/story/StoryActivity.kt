package com.segalahal.dicodingstoryapp.ui.story

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.segalahal.dicodingstoryapp.R
import com.segalahal.dicodingstoryapp.databinding.ActivityStoryBinding
import com.segalahal.dicodingstoryapp.ui.addstory.AddStoryActivity
import com.segalahal.dicodingstoryapp.ui.login.LoginActivity
import com.segalahal.dicodingstoryapp.ui.map.MapsActivity
import com.segalahal.dicodingstoryapp.viewmodel.ViewModelFactory

class StoryActivity : AppCompatActivity() {
    private var _binding : ActivityStoryBinding? = null
    private lateinit var viewModel: StoryViewModel
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[StoryViewModel::class.java]
        val storiesAdapter = StoriesAdapter()

        val preferences = this.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val bearer = "Bearer " + preferences.getString("token", "").toString()

        binding.rvStories.layoutManager = LinearLayoutManager(this)
        viewModel.getStories(bearer).observe(this){
            binding.rvStories.adapter = storiesAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storiesAdapter.retry()
                }
            )
            viewModel.getStories(bearer).observe(this) {
                storiesAdapter.submitData(lifecycle, it)
            }
        }

        binding.btnAddStory.setOnClickListener {
            val i = Intent(this@StoryActivity, AddStoryActivity::class.java)
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_logout -> {
                Toast.makeText(
                    this,
                    "Logout berhasil!",
                    Toast.LENGTH_SHORT).show()
                viewModel.deleteUser()
                val i = Intent(this, LoginActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
                finish()
                true
            }
            R.id.btn_map -> {
                val i = Intent(this@StoryActivity, MapsActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}