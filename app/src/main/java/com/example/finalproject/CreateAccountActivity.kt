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

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var emailET:EditText
    private lateinit var passwordET: EditText
    private lateinit var passwordConfirmET:EditText
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
        emailET = findViewById(R.id.etEmail)
        passwordET = findViewById(R.id.etPassword)
        passwordConfirmET = findViewById(R.id.etPasswordConfirm)
        auth = FirebaseAuth.getInstance()
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        signUpButton.setOnClickListener{
            var email = emailET.text.toString()
            var password = passwordET.text.toString()
            var passwordConfirm = passwordConfirmET.text.toString()
            if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()){
                Toast.makeText(this, "Please fill in all required values", Toast.LENGTH_SHORT).show()
            }
            if (password != passwordConfirm){
                Toast.makeText(this, "Passwords must match", Toast.LENGTH_SHORT).show()
                passwordConfirmET.error = "Passwords must match"
            }
            else {
                passwordConfirmET.error = null
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this, "Sign up unsuccessful", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}