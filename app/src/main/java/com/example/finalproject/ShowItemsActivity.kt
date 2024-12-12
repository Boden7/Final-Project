package com.example.finalproject

/*
 * ShowItemsActivity.kt
 * Android App Development - CSCI 380
 * By Jordan Brown and Boden Kahn

 * This file provides the ability for users to view a specific item on the menu by retrieving it
 * from the firebase database
*/

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase

class ShowItemsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_show_items)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Declare variables for the text views
        val nameTV = findViewById<TextView>(R.id.menuItem)
        val caloriesTV = findViewById<TextView>(R.id.caloriesText)
        val ingredientsTV = findViewById<TextView>(R.id.ingredientsText)
        val priceTV = findViewById<TextView>(R.id.priceText)
        val name = intent.getStringExtra("NAME")

        // Initialize the database and get the reference
        val database = FirebaseDatabase.getInstance()

        val myRef = database.getReference("menu")
        // Get the item from the database
        if (name != null) {
            myRef.child(name).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val item = snapshot.getValue(MenuItem::class.java)
                    // Update the text views with the values from the database
                    if (item != null){
                        nameTV.text = name
                        val calories = item.calories.toString() + " calories"
                        caloriesTV.text = calories
                        ingredientsTV.text = item.ingredients
                        val price = "$" + item.price.toString()
                        priceTV.text = price
                    }
                    // Give an error message if the item doesn't exist
                    else {
                        Toast.makeText(this, "Item doesn't exist", Toast.LENGTH_SHORT).show()
                    }
                }
                // Give an error message if there is an issue with getting the data from the database
                else {
                    Toast.makeText(this, "Error reading data", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Create the return button to go back to the selection page
        val returnButton = findViewById<Button>(R.id.returnButton)

        // Set up the back button to go back to the selection activity
        returnButton.setOnClickListener{
            val selectionActivityIntent = Intent(this, SelectionActivity::class.java)
            startActivity(selectionActivityIntent)
            finish()
        }
    }
}