"""
 * LoginActivity.kt
 * Android App Development - CSCI 380
 * By Jordan Brown and Boden Kahn

 * This file provides authentication to our menu app
"""


package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var emailET: TextInputEditText
    private lateinit var passwordET: TextInputEditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Initializes the firebase authentication and the email and password text input edit texts
        auth = FirebaseAuth.getInstance()
        emailET = findViewById(R.id.tietEmail)
        passwordET = findViewById(R.id.tietPassword)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initializes and adds a listener to the create account button to take users to the intent
        // where they can create accounts for our menu app
        val createAccountButton = findViewById<Button>(R.id.createAccountButton)
        createAccountButton.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        // Initializes and adds a listener to the login button that attempts to authenticate users
        // into our menu app
        val loginButton = findViewById<Button>(R.id.btnLogin)
        loginButton.setOnClickListener{

            // Gets the users email and password from the text input edit texts
            val email = emailET.text.toString()
            val password = passwordET.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                // If either the email or password was empty a toast is shown informing users that
                // they must provide input into both values
                Toast.makeText(this, "Please fill in all required values", Toast.LENGTH_SHORT).show()
            }
            else{
                // Otherwise we make a call to the firebase authentication tool to attempt and
                // authenticate a user
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){task ->
                    if (task.isSuccessful) {
                        // If this authentication is successful the users are shown a toast
                        // informing them that they have been authenticated before taking them to
                        // the selection activity
                        Toast.makeText(this, "Authentication successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SelectionActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        // If the user could not be authenticated they are shown a toast
                        // informing them that they were not able to be authenticated
                        Toast.makeText(this, "Authentication unsuccessful", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}