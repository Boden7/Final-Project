package com.example.finalproject

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
    private lateinit var taskDatabaseHelper: TaskDatabaseHelper
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var addTaskButton: Button
    private lateinit var taskEditText: EditText
    private var tasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        addTaskButton = findViewById(R.id.addButton)
        taskEditText = findViewById(R.id.editText)
        taskDatabaseHelper = TaskDatabaseHelper.getInstance(this)

        //On click listener for the add task button to add a task and update the RV
        addTaskButton.setOnClickListener{
            //Get the task text from the user's input
            val task = taskEditText.text.toString().trim()
            if (task.isNotEmpty()) {
                //Add the task to the database
                taskDatabaseHelper.addTask(task)
                //Update the task list
                tasks = taskDatabaseHelper.getAllTasks().toMutableList()
                //Update the RV
                taskAdapter.updateTasks(tasks)
                //Clear the text to prepare for the next input
                taskEditText.text.clear()
            }
        }

        //Function to update a task, update it in the task list, & update the RV
        fun update(task: Task, isChecked: Boolean){
            //Get the task from the list and update its check value
            val taskInList = tasks.find {it.id == task.id}
            if (taskInList != null && taskInList.id == task.id && taskInList.task == task.task) {
                taskInList.isChecked = isChecked
                //Update the task in the database
                taskDatabaseHelper.updateTask(task.id, isChecked)
                //Update the RV when it is ready
                taskRecyclerView.post {
                    taskAdapter.updateTasks(tasks)
                }
            }
        }

        //Function to delete a task, remove it from the task list, & update the RV
        fun delete(task: Task){
            //Delete the task from the RV
            taskDatabaseHelper.deleteTask(task.id)
            //Remove it from the task list
            tasks.remove(task)
            //Update the RV
            taskAdapter.updateTasks(tasks)
        }

        //Creating the RV and its adapter
        taskRecyclerView = findViewById(R.id.rvData)
        taskAdapter = TaskAdapter(this, tasks, {task, isChecked -> update(task, isChecked)},
            {task -> delete(task)})
        taskRecyclerView.adapter = taskAdapter
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        tasks = taskDatabaseHelper.getAllTasks().toMutableList()
        taskAdapter.updateTasks(tasks)
    }
}