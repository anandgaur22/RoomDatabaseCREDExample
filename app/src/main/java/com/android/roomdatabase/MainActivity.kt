package com.android.roomdatabase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var buttonAdd: Button
    private lateinit var buttonUpdate: Button
    private lateinit var buttonDelete: Button
    private lateinit var recyclerView: RecyclerView

    private var currentUser: User? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        userAdapter = UserAdapter { user -> onUserClicked(user) }
        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Views
        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        buttonAdd = findViewById(R.id.buttonAdd)
        buttonUpdate = findViewById(R.id.buttonUpdate)
        buttonDelete = findViewById(R.id.buttonDelete)

        // Initialize Database and ViewModel
        val userDao = UserDatabase.getDatabase(application).userDao()
        val userRepository = UserRepository(userDao)
        userViewModel = UserViewModel(userRepository)

        // Fetch all users when the activity starts
        userViewModel.fetchAllUsers()
        userViewModel.allUsers.observe(this) { users ->
            userAdapter.submitList(users) // Use submitList here
        }

        // Add User Button Click
        buttonAdd.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                val user = User(0, name, email) // ID is auto-generated
                userViewModel.addUser(user)
                editTextName.text.clear()
                editTextEmail.text.clear()
                Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show()
                userViewModel.fetchAllUsers()

            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }


        }

        // Update User Button Click
        buttonUpdate.setOnClickListener {
            currentUser?.let {
                val name = editTextName.text.toString()
                val email = editTextEmail.text.toString()

                if (name.isNotEmpty() && email.isNotEmpty()) {
                    val updatedUser = it.copy(name = name, email = email)
                    userViewModel.updateUser(updatedUser)
                    editTextName.text.clear()
                    editTextEmail.text.clear()
                    Toast.makeText(this, "User updated", Toast.LENGTH_SHORT).show()

                    userViewModel.fetchAllUsers()

                } else {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "No user selected for update", Toast.LENGTH_SHORT).show()
            }
        }

        // Delete User Button Click
        buttonDelete.setOnClickListener {
            currentUser?.let {
                userViewModel.deleteUser(it)
                editTextName.text.clear()
                editTextEmail.text.clear()
                Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show()
                currentUser = null // Clear the current user

                userViewModel.fetchAllUsers()

            } ?: run {
                Toast.makeText(this, "No user selected for deletion", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onUserClicked(user: User) {
        currentUser = user
        editTextName.setText(user.name)
        editTextEmail.setText(user.email)
    }
}