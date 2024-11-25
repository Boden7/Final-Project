package com.example.finalproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val context: Context, private var tasks: List<MenuItem>,
                  private val onTaskCheckedChange: (MenuItem, Boolean) -> Unit,
                  private var onDeleteClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.menu_item,
            parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<MenuItem>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val caloriesText: TextView = itemView.findViewById(R.id.caloriesText)
        private val menuText: TextView = itemView.findViewById(R.id.menuItem)
        private val detailsButton: ImageView = itemView.findViewById(R.id.detailsButton)

        fun bind(menuItem: MenuItem) {
            menuText.text = menuItem.name
            caloriesText.text = menuItem.calories.toString()
            detailsButton.setOnClickListener {
                onDeleteClick(menuItem)
            }
        }
    }
}