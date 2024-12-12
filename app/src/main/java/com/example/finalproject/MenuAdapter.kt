package com.example.finalproject

/*
 * MenuAdapter.kt
 * Android App Development - CSCI 380
 * By Jordan Brown and Boden Kahn

 * This file updates the database and recycler view whenever an item is added or deleted
*/

import android.content.Context
import android.view.LayoutInflater
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

    // Function to update the database
    fun updateItems() {
        //Get the instance from the database and the reference
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("menu")

        // Add the event listener to watch for changes
        myRef.addChildEventListener(object: ChildEventListener {
            //When an item is added, add it to the list and update the recycler view
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(MenuItem::class.java)
                if (item != null){
                    items.add(item)
                    notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                //Not used
            }

            // When an item is removed, remove it from the list and update the recycler view
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val item = snapshot.getValue(MenuItem::class.java)
                if (item != null){
                    items.remove(item)
                    notifyDataSetChanged()
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //Not used
            }

            override fun onCancelled(error: DatabaseError) {
                //Use if needed
            }
        })
    }

    inner class TaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        // Initialize variables for the text views and more info button
        private val priceText: TextView = itemView.findViewById(R.id.priceText)
        private val menuText: TextView = itemView.findViewById(R.id.menuItem)
        private val detailsButton: ImageView = itemView.findViewById(R.id.detailsButton)

        fun bind(menuItem: MenuItem, listener: OnItemClickListener) {
            // Set the text views to the values for name and price
            menuText.text = menuItem.name
            priceText.text = menuItem.price.toString()
            // Set the listener for the more details button
            detailsButton.setOnClickListener {
                listener.onItemClick(menuItem)
            }
        }
    }
}