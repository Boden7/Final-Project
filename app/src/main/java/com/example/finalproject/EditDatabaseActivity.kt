"""
 * EditDatabaseActivity.kt
 * Android App Development - CSCI 380
 * By Jordan Brown and Boden Kahn

 * This file provides the ability for users to add, delete and search for specific items while also
 * allowing them to travel to another intent to view the entire menu stored in our database
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
import com.google.firebase.database.FirebaseDatabase

class EditDatabaseActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etCalories: EditText
    private lateinit var etIngredients: EditText
    private lateinit var etPrice: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_database)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Gets the edit text boxes needed to add, delete and search menu items
        etName = findViewById(R.id.etName)
        etCalories = findViewById(R.id.etCalories)
        etIngredients = findViewById(R.id.etIngredients)
        etPrice = findViewById(R.id.etPrice)

        // Initializes the buttons needed to add, delete, search and display menu items
        var addButton = findViewById<Button>(R.id.btnAdd)
        var searchButton = findViewById<Button>(R.id.btnSearch)
        var deleteButton = findViewById<Button>(R.id.btnDelete)
        var displayButton = findViewById<Button>(R.id.btnDisplay)

        // Creates an on click listener for the addition button that attempts to add a menu item
        // to the database
        addButton.setOnClickListener{

            // Initializes the variables containing the menu items name, calories, ingredients and
            // price from the edit text boxes
            val name = etName.text.toString()
            val caloriesInput = etCalories.text.toString()
            val ingredients = etIngredients.text.toString()
            val priceInput = etPrice.text.toString()

            // If those boxes were empty and a value could not be found, a toast is printed
            // to inform the users that they must fill all text boxes
            if (name.isEmpty() || caloriesInput.isEmpty() || ingredients.isEmpty() || priceInput.isEmpty()){
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
            else {
                // Otherwise we initialize the firebase database, go to the menu schema within it
                // and attempt to add the menu item created from the user input to it
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("menu")
                val price = priceInput.toDouble()
                val calories = caloriesInput.toInt()
                val menuItem = MenuItem(name, calories, ingredients, price)
                myRef.child(name).setValue(menuItem).addOnSuccessListener {
                    // If the item was successfully added we show a toast informing the user of its
                    // addition
                    Toast.makeText(this, "Menu Item Added", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    // Otherwise, on a failure we show a toast informing users it could not be added
                    Toast.makeText(this, "Menu Item Not Added", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Adds a listener to the delete button that attempts to delete an item based on the
        // provided name
        deleteButton.setOnClickListener {

            // Initializes the name of the object to be deleted
            val name = etName.text.toString()

            if (name.isEmpty()) {
                // If the name was empty a toast is printed informing users that they must fill out
                // the name field
                Toast.makeText(this, "Please fill out the name field", Toast.LENGTH_SHORT).show()
            } else {
                // Otherwise, we attempt to delete the named object from the database
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("menu")
                val itemRef = myRef.child(name)

                itemRef.removeValue().addOnSuccessListener {
                    // If the item was successfully added to the database we show a toast informing
                    // the user of its deletion
                    Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    // Otherwise, we print a toast showing that the item could not be deleted
                    Toast.makeText(this, "Error deleting item", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Adds a listener to the search button
        searchButton.setOnClickListener{

            // Gets the name of the item in the database the user wishes to search
            val name = etName.text.toString()

            if (name.isEmpty()){
                // If the name provided was blank a toast is printed informing them that the name
                // field must be filled out
                Toast.makeText(this, "Please fill out name field", Toast.LENGTH_SHORT).show()
            }
            else {
                // If the name was filled out we take the users to a the show items activity and
                // send the name of the item to grab from the database with the second activity
                // intent
                val intent = Intent(this, ShowItemsActivity::class.java)
                intent.putExtra("NAME", name)
                startActivity(intent)
            }
        }

        // Adds a listener to the display button to take users to the main activity that is our
        // recycler view
        displayButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Adds a listner to the back button to return users to the selection activity page where
        // they can choose to meet the chef, display all items or logout in addition to returning
        // to this activity
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener{
            val intent = Intent(this, SelectionActivity::class.java)
            startActivity(intent)
        }
    }
}