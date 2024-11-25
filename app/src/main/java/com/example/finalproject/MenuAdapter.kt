package com.example.finalproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val context: Context, private var tasks: List<Task>,
                  private val onTaskCheckedChange: (Task, Boolean) -> Unit,
                  private var onDeleteClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_item,
            parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val taskCheckBox: CheckBox = itemView.findViewById(R.id.taskCheckBox)
        private val taskTitle: TextView = itemView.findViewById(R.id.taskTitle)
        private val detailsButton: ImageView = itemView.findViewById(R.id.detailsButton)

        fun bind(task: Task) {
            taskTitle.text = task.task
            taskCheckBox.isChecked = task.isChecked

            taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
                onTaskCheckedChange(task, isChecked)
            }
            detailsButton.setOnClickListener {
                onDeleteClick(task)
            }
        }
    }
}