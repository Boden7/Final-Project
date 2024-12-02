package com.example.finalproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class AdminPage : AppCompatActivity() {
    private lateinit var addItemButton: Button
    private lateinit var itemEditText: EditText
    private lateinit var menuDatabaseHelper: MenuDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        addItemButton = findViewById(R.id.addButton)
        itemEditText = findViewById(R.id.editTextName)
        menuDatabaseHelper = MenuDatabaseHelper.getInstance(this)

        //On click listener for the add task button to add a task and update the RV
        addItemButton.setOnClickListener{
            //Get the task text from the user's input
            val item = itemEditText.text.toString().trim()
            val calories = 100
            if (item.isNotEmpty()) {
                //Add the task to the database
                menuDatabaseHelper.addItem(item, calories, ingredients, description)
                //Update the task list
                items = menuDatabaseHelper.getAllItems().toMutableList()
                //Update the RV
                menuAdapter.updateItems(items)
                //Clear the text to prepare for the next input
                itemEditText.text.clear()
            }
    }
}