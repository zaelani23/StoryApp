package com.segalahal.dicodingstoryapp.ui.login

import androidx.lifecycle.ViewModel
import com.segalahal.dicodingstoryapp.data.Repository

class LoginViewModel(private val repository: Repository) : ViewModel(){
    fun login(email: String, password: String) = repository.login(email, password)

    fun getUser() = repository.getUser()
}