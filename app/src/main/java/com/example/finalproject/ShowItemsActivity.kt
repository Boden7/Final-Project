package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
        val name = intent.getStringExtra("NAME")
        val calories = intent.getStringExtra("CALORIES")
        val ingredients = intent.getStringExtra("INGREDIENTS")
        val description = intent.getStringExtra("DESCRIPTION")

        val nameTV = findViewById<TextView>(R.id.menuItem)
        val caloriesTV = findViewById<TextView>(R.id.caloriesText)
        val ingredientsTV = findViewById<TextView>(R.id.ingredientsText)
        val descriptionTV = findViewById<TextView>(R.id.descriptionText)

        nameTV.text = name
        caloriesTV.text = calories
        ingredientsTV.text = ingredients
        descriptionTV.text = description

        val returnButton = findViewById<Button>(R.id.returnButton)

        returnButton.setOnClickListener{
            val mainActiviyIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActiviyIntent)
            finish()
        }
    }
}