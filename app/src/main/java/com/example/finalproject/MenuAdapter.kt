package com.example.finalproject

import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class MenuAdapter(private val context: Context, private var items: MutableList<MenuItem>,
                  private val listener: OnItemClickListener) :
                  RecyclerView.Adapter<MenuAdapter.TaskViewHolder>() {
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

    fun updateItems() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("menu")
        myRef.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(MenuItem::class.java)
                if (item != null){
                    items.add(item)
                    notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val item = snapshot.getValue(MenuItem::class.java)
                if (item != null){
                    items.remove(item)
                    notifyDataSetChanged()
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //Use if needed
            }

            override fun onCancelled(error: DatabaseError) {
                //Use if needed
            }
        })

        //var newItemsList: MutableList<MenuItem>

        //items = newItemsList

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