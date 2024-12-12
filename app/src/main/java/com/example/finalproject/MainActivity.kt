package com.example.finalproject

/*
 * MainActivity.kt
 * Android App Development - CSCI 380
 * By Jordan Brown and Boden Kahn

 * This file houses the recycler view that displays our menu and the items within it
*/

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), MenuAdapter.OnItemClickListener {
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var menuRecyclerView: RecyclerView

    // List of all items stored in the menu database
    private var items = mutableListOf<MenuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Creating the recycler view and its adapter
        menuRecyclerView = findViewById(R.id.rvData)
        menuAdapter = MenuAdapter(this, items, this)
        menuRecyclerView.adapter = menuAdapter
        menuRecyclerView.layoutManager = LinearLayoutManager(this)

        menuAdapter.updateItems()

        // Initializes and adds a listener for the back button to take users back to the selection
        // activity
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, SelectionActivity::class.java)
            startActivity(intent)
        }
    }

    // When an item in the recycler view is clicked the users are taken to the show items activity
    // that shows the menu item's name, calories, price and ingredients
    override fun onItemClick(menuItem: MenuItem) {
        Toast.makeText(this, "Clicked ${menuItem.name}", Toast.LENGTH_SHORT).show()
        val detailsActivityIntent = Intent(this, ShowItemsActivity::class.java)
        detailsActivityIntent.putExtra("NAME", menuItem.name)
        startActivity(detailsActivityIntent)
    }
}