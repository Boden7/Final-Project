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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), MenuAdapter.OnItemClickListener {
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var menuRecyclerView: RecyclerView

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

        //Creating the RV and its adapter
        menuRecyclerView = findViewById(R.id.rvData)
        menuAdapter = MenuAdapter(this, items, this)
        menuRecyclerView.adapter = menuAdapter
        menuRecyclerView.layoutManager = LinearLayoutManager(this)

        menuAdapter.updateItems()

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, SelectionActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(menuItem: MenuItem) {
        Toast.makeText(this, "Clicked ${menuItem.name}", Toast.LENGTH_SHORT).show()
        val detailsActivityIntent = Intent(this, ShowItemsActivity::class.java)
        detailsActivityIntent.putExtra("NAME", menuItem.name)
        startActivity(detailsActivityIntent)
    }
}