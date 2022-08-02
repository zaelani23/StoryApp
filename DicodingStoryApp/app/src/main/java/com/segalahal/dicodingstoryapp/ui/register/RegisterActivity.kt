package com.segalahal.dicodingstoryapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.segalahal.dicodingstoryapp.R
import com.segalahal.dicodingstoryapp.databinding.ActivityRegisterBinding
import com.segalahal.dicodingstoryapp.ui.login.LoginActivity
import com.segalahal.dicodingstoryapp.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private var _binding : ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        binding.btnRegister.setOnClickListener{
            markButtonDisable(binding.btnRegister, false)
            viewModel.register(
                binding.inputName.text.toString(),
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString()
            ).observe(this){register ->
                if (register.error){
                    Toast.makeText(
                        this@RegisterActivity,
                        register.message,
                        Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(
                        this@RegisterActivity,
                        "Register berhasil, silakan login!",
                        Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                markButtonDisable(binding.btnRegister, true)
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