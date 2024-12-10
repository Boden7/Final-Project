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
    private lateinit var etDescription: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_database)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etName = findViewById(R.id.etName)
        etCalories = findViewById(R.id.etCalories)
        etIngredients = findViewById(R.id.etIngredients)
        etDescription = findViewById(R.id.etDescription)

        var addButton = findViewById<Button>(R.id.btnAdd)
        var searchButton = findViewById<Button>(R.id.btnSearch)
        var deleteButton = findViewById<Button>(R.id.btnDelete)
        var displayButton = findViewById<Button>(R.id.btnDisplay)

        addButton.setOnClickListener{
            val name = etName.text.toString()
            val caloriesInput = etCalories.text.toString()
            val ingredients = etIngredients.text.toString()
            val description = etDescription.text.toString()
            if (name.isEmpty() || caloriesInput.isEmpty() || ingredients.isEmpty() || description.isEmpty()){
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
            else {
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("menu")
                val calories = caloriesInput.toInt()
                val menuItem = MenuItem(name, calories, ingredients, description)
                myRef.child(name).setValue(menuItem).addOnSuccessListener {
                    Toast.makeText(this, "Menu Item Added", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(this, "Menu Item Not Added", Toast.LENGTH_SHORT).show()
                }
            }
        }
        deleteButton.setOnClickListener {
            val name = etName.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else {
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("menu")
                val itemRef = myRef.child(name)
                itemRef.removeValue().addOnSuccessListener {
                    Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(this, "Error deleting item", Toast.LENGTH_SHORT).show()
                }
            }
        }
        searchButton.setOnClickListener{
            val name = etName.text.toString()
            if (name.isEmpty()){
                Toast.makeText(this, "Please fill out name field", Toast.LENGTH_SHORT).show()
            }
            else {
                val intent = Intent(this, ShowItemsActivity::class.java)
                intent.putExtra("NAME", name)
                startActivity(intent)
            }
        }
        displayButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener{
            val intent = Intent(this, SelectionActivity::class.java)
            startActivity(intent)
        }
    }
}