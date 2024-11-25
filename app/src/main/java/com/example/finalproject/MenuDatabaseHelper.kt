package com.example.finalproject

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MenuDatabaseHelper(applicationContext: Context) : SQLiteOpenHelper(applicationContext, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        private const val DATABASE_NAME = "items.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "items"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_CALORIES = "calories"

        @Volatile
        private var INSTANCE: MenuDatabaseHelper? = null

        // Singleton instance
        fun getInstance(context: Context): MenuDatabaseHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MenuDatabaseHelper(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase){
        val createTableStatement = ("CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NAME TEXT, $COLUMN_CALORIES INTEGER DEFAULT 0)")
        db.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int){
        // This method is called when the database needs to be upgraded.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    //Add a task to the database
    fun addItem(name: String, calories: Int){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COLUMN_NAME, name)
        cv.put(COLUMN_CALORIES ,calories)
        db.insert(TABLE_NAME, null, cv)
    }

    //Get all tasks as a list of tasks from the database
    fun getAllItems(): List<MenuItem> {
        val itemList = mutableListOf<MenuItem>()
        val db = this.readableDatabase
        //Create the cursor for the database
        val cursor: Cursor = db.query(
            TABLE_NAME, arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_CALORIES),
            null, null, null, null, null
        )
        if (cursor.moveToFirst()) {
            do {
                val taskIdIndex = cursor.getColumnIndex(COLUMN_ID)
                val taskNameIndex = cursor.getColumnIndex(COLUMN_NAME)
                val isCheckedIndex = cursor.getColumnIndex(COLUMN_CALORIES)
                // Check if the indexes are valid
                if (taskIdIndex != -1 && taskNameIndex != -1 && isCheckedIndex != -1) {
                    val id = cursor.getInt(taskIdIndex)
                    val taskName = cursor.getString(taskNameIndex)
                    val calories = cursor.getInt(isCheckedIndex)
                    val task = MenuItem(id, taskName, calories)
                    //Add the item to the list
                    itemList.add(task)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return itemList
    }

    // Update task's checked state in the database
    fun updateItem(id: Int, calories: Int) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_CALORIES, calories)
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    //Delete task from the database
    fun deleteItem(id: Int){
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

}