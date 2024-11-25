package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var menuDatabaseHelper: MenuDatabaseHelper
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var addItemButton: Button
    private lateinit var itemEditText: EditText
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
        addItemButton = findViewById(R.id.addButton)
        itemEditText = findViewById(R.id.editText)
        menuDatabaseHelper = MenuDatabaseHelper.getInstance(this)

        //On click listener for the add task button to add a task and update the RV
        addItemButton.setOnClickListener{
            //Get the task text from the user's input
            val item = itemEditText.text.toString().trim()
            val calories = 100
            if (item.isNotEmpty()) {
                //Add the task to the database
                menuDatabaseHelper.addItem(item, calories)
                //Update the task list
                items = menuDatabaseHelper.getAllItems().toMutableList()
                //Update the RV
                menuAdapter.updateItems(items)
                //Clear the text to prepare for the next input
                itemEditText.text.clear()
            }
        }

        //Function to update a task, update it in the task list, & update the RV
        fun update(item: MenuItem, calories: Int){
            //Get the task from the list and update its check value
            val itemInList = items.find {it.name == item.name}
            if (itemInList != null && itemInList.name == item.name) {
                itemInList.calories = calories
                //Update the task in the database
                menuDatabaseHelper.updateItem(item.name, calories)
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
        menuAdapter = MenuAdapter(this, items, {item, calories -> update(item, calories)},
            {item -> delete(item)})
        menuRecyclerView.adapter = menuAdapter
        menuRecyclerView.layoutManager = LinearLayoutManager(this)
        items = menuDatabaseHelper.getAllItems().toMutableList()
        menuAdapter.updateItems(items)
    }
}