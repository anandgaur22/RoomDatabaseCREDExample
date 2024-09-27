package com.android.roomdatabase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _allUsers = MutableLiveData<List<User>>()
    val allUsers: LiveData<List<User>> get() = _allUsers

    fun addUser(user: User) {
        userRepository.insertUser(user)
    }

    fun updateUser(user: User) {
        userRepository.updateUser(user)
    }

    fun deleteUser(user: User) {
        userRepository.deleteUser(user)
    }

    fun fetchAllUsers() {
        userRepository.getAllUsers { users ->
            // Post the result back to the LiveData
            _allUsers.postValue(users)
        }
    }
}