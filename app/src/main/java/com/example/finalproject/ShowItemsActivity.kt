package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.ImageView
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
        val nameTV = findViewById<TextView>(R.id.menuItem)
        val caloriesTV = findViewById<TextView>(R.id.caloriesText)
        val ingredientsTV = findViewById<TextView>(R.id.ingredientsText)
        val priceTV = findViewById<TextView>(R.id.priceText)
        val name = intent.getStringExtra("NAME")

        val database = FirebaseDatabase.getInstance()

        val myRef = database.getReference("menu")
        if (name != null) {
            myRef.child(name).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val item = snapshot.getValue(MenuItem::class.java)
                    if (item != null){
                        nameTV.text = name
                        val calories = item.calories.toString() + " calories"
                        caloriesTV.text = calories
                        ingredientsTV.text = item.ingredients
                        val price = "$" + item.price.toString()
                        priceTV.text = price
                    }
                    else {
                        Toast.makeText(this, "User doesn't exist", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(this, "Error reading data", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val returnButton = findViewById<Button>(R.id.returnButton)

        returnButton.setOnClickListener{
            val selectionActivityIntent = Intent(this, SelectionActivity::class.java)
            startActivity(selectionActivityIntent)
            finish()
        }
    }
}