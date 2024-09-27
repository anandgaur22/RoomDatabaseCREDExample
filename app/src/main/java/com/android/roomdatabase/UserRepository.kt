package com.android.roomdatabase

class UserRepository(private val userDao: UserDao) {

    fun insertUser(user: User) {
        // Run insert operation in a separate thread
        Thread {
            userDao.insertUser(user)
        }.start()
    }

    fun updateUser(user: User) {
        // Run update operation in a separate thread
        Thread {
            userDao.updateUser(user)
        }.start()
    }

    fun deleteUser(user: User) {
        // Run delete operation in a separate thread
        Thread {
            userDao.deleteUser(user)
        }.start()
    }

    fun getAllUsers(callback: (List<User>) -> Unit) {
        // Run query operation in a separate thread
        Thread {
            val users = userDao.getAllUsers()
            // Pass the results back to the callback
            callback(users)
        }.start()
    }
}