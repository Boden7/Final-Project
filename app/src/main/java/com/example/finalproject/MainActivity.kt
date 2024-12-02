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
    private lateinit var menuDatabaseHelper: MenuDatabaseHelper
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

        //Function to update a task, update it in the task list, & update the RV
        fun update(item: MenuItem, name: String, calories: Int, ingredients: String, description: String){
            //Get the task from the list and update its check value
            val itemInList = items.find {it.name == item.name}
            if (itemInList != null && itemInList.name == item.name) {
                itemInList.calories = calories
                //Update the task in the database
                menuDatabaseHelper.updateItem(item.id, name, calories, ingredients, description)
                //Update the RV when it is ready
                menuRecyclerView.post {
                    menuAdapter.updateItems(items)
                }
            }
        }

        //Function to delete an item, remove it from the item list, & update the RV
        fun delete(item: MenuItem){
            //Delete the item from the RV
            menuDatabaseHelper.deleteItem(item.id)
            //Remove it from the item list
            items.remove(item)
            //Update the RV
            menuAdapter.updateItems(items)
        }

        val button = findViewById<Button>(R.id.meetUsButton)
        button.setOnClickListener {
            val intent = Intent(this, MeetTheChef::class.java)
            startActivity(intent)
        }

        //Creating the RV and its adapter
        menuRecyclerView = findViewById(R.id.rvData)
        menuAdapter = MenuAdapter(this, items, this, {item, name, calories, ingredients, details -> update(item, name, calories, ingredients, details)})
        menuRecyclerView.adapter = menuAdapter
        menuRecyclerView.layoutManager = LinearLayoutManager(this)
        items = menuDatabaseHelper.getAllItems().toMutableList()
        menuAdapter.updateItems(items)
    }

    override fun onItemClick(menuItem: MenuItem) {
        Toast.makeText(this, "Clicked ${menuItem.name}", Toast.LENGTH_SHORT).show()
        val detailsActivityIntent = Intent(this, ShowItemsActivity::class.java)
        detailsActivityIntent.putExtra("MENU_ID", menuItem.id)
        startActivity(detailsActivityIntent)
    }
}