"""
 * CreateAccountActivity.kt
 * Android App Development - CSCI 380
 * By Jordan Brown and Boden Kahn

 * This file adds functionality for the creation of user accounts to eventually allow authentication
 * into our menu app
"""

package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var emailET:TextInputEditText
    private lateinit var passwordET: TextInputEditText
    private lateinit var passwordConfirmET:TextInputEditText
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_account)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Gets the text input edit texts from the layout and initializes firebase
        emailET = findViewById(R.id.tietEmail)
        passwordET = findViewById(R.id.tietPassword)
        passwordConfirmET = findViewById(R.id.tietPasswordConfirm)
        auth = FirebaseAuth.getInstance()
        val backButton = findViewById<Button>(R.id.backButton)

        // Adds listener to the back button to return to the login screen if the user does not
        // wish to create an account
        backButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Initializes and adds listener to sign up button
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        signUpButton.setOnClickListener{

            // Gets the text out of the email, password and password confirmation text inputs
            var email = emailET.text.toString()
            var password = passwordET.text.toString()
            var passwordConfirm = passwordConfirmET.text.toString()

            // Ensures that the text inputs are not empty
            if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()){
                // If empty shows a toast indicating the need to fill all text boxes
                Toast.makeText(this, "Please fill in all required values", Toast.LENGTH_SHORT).show()
            }

            // Compares the provided password and confirmation password to ensure equality
            if (password != passwordConfirm){
                // If not equal prompts users to input matching passwords
                Toast.makeText(this, "Passwords must match", Toast.LENGTH_SHORT).show()
                passwordConfirmET.error = "Passwords must match"
            }
            else {
                // If passwords were equal we attempt to add a new user to the firebase
                // authentication tool
                passwordConfirmET.error = null
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // If successful we show a toast letting users know that their account was
                        // added and return them to the login page to begin logging in
                        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        // Otherwise if the account creation was unsuccessful we show a toast
                        // informing users that their account could not be created
                        Toast.makeText(this, "Sign up unsuccessful", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}