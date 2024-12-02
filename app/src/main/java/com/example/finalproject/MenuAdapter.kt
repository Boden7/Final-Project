package com.example.finalproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(private val context: Context, private var items: List<MenuItem>,
                  private val listener: OnItemClickListener,
                  private var addToCart: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.TaskViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(menuItem: MenuItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.menu_item,
            parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, listener)
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<MenuItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val caloriesText: TextView = itemView.findViewById(R.id.caloriesText)
        private val menuText: TextView = itemView.findViewById(R.id.menuItem)
        private val detailsButton: ImageView = itemView.findViewById(R.id.detailsButton)

        fun bind(menuItem: MenuItem, listener: OnItemClickListener) {
            menuText.text = menuItem.name
            caloriesText.text = menuItem.calories.toString()
            detailsButton.setOnClickListener {
                listener.onItemClick(menuItem)
            }
        }
    }
}