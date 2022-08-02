package com.segalahal.dicodingstoryapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.segalahal.dicodingstoryapp.R
import com.segalahal.dicodingstoryapp.databinding.ActivityLoginBinding
import com.segalahal.dicodingstoryapp.ui.register.RegisterActivity
import com.segalahal.dicodingstoryapp.ui.story.StoryActivity
import com.segalahal.dicodingstoryapp.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private var _binding : ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gotoRegister.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        binding.btnLogin.setOnClickListener{
            markButtonDisable(binding.btnLogin, false)
            viewModel.login(
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString()
            ).observe(this) { login ->
                if (login.error) {
                    Toast.makeText(
                        this@LoginActivity,
                        login.message,
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login berhasil!",
                        Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, StoryActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                markButtonDisable(binding.btnLogin, true)
            }
        }
    }

    private fun markButtonDisable(button: Button, isEnable: Boolean) {
        button.isEnabled = isEnable
        if (isEnable){
            button.background = ContextCompat.getDrawable(applicationContext, R.drawable.round_bg)
        }else {
            button.background = ContextCompat.getDrawable(applicationContext, R.drawable.round_bg_disable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}