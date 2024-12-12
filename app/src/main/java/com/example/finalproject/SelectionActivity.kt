package com.example.finalproject

/*
 * SelectionActivity.kt
 * Android App Development - CSCI 380
 * By Jordan Brown and Boden Kahn

 * This file provides the ability for users to go to view the chefs, view the menu, or edit the menu
*/

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class SelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_selection)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the database
        var auth = FirebaseAuth.getInstance()

        // Create the button to go to the meet the chef page
        val meetUsButton = findViewById<ImageView>(R.id.meetUsButton)
        meetUsButton.setOnClickListener {
            val intent = Intent(this, MeetTheChef::class.java)
            startActivity(intent)
        }

        // Create the button to go to the page that displays the recycler view
        val displayButton = findViewById<Button>(R.id.viewButton)
        displayButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Create the button to go to the page that can edit the database
        val editButton = findViewById<Button>(R.id.editButton)
        editButton.setOnClickListener {
            val intent = Intent(this, EditDatabaseActivity::class.java)
            startActivity(intent)
        }

        // Create the button that signs out the user and returns to the login page
        val signOutButton = findViewById<Button>(R.id.signOutButton)
        signOutButton.setOnClickListener {
            Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show()
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}