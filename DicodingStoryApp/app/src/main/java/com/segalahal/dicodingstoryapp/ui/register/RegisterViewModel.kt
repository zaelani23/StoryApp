package com.segalahal.dicodingstoryapp.ui.register

import androidx.lifecycle.ViewModel
import com.segalahal.dicodingstoryapp.data.Repository

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        repository.register(name, email, password)
}