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
        auth = FirebaseAuth.getInstance()
        emailET = findViewById(R.id.tietEmail)
        passwordET = findViewById(R.id.tietPassword)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val backButton = findViewById<Button>(R.id.createAccountButton)
        backButton.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        val loginButton = findViewById<Button>(R.id.btnLogin)
        loginButton.setOnClickListener{
            val email = emailET.text.toString()
            val password = passwordET.text.toString()
            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please fill in all required values", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Authentication successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SelectionActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this, "Authentication unsuccessful", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}