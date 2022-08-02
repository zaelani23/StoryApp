package com.segalahal.dicodingstoryapp.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.segalahal.dicodingstoryapp.R
import com.segalahal.dicodingstoryapp.ui.login.LoginActivity
import com.segalahal.dicodingstoryapp.ui.login.LoginViewModel
import com.segalahal.dicodingstoryapp.ui.story.StoryActivity
import com.segalahal.dicodingstoryapp.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getUser().observe(this){user->
                if (user.userId.isEmpty()){
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this, StoryActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 2000)
    }
}