package com.elchinasgarov.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.elchinasgarov.data.models.ToDoData

@Dao
interface ToDoDao {

    @Query("SELECT * FROM TODO_TABLE ORDER BY id ASC")
    fun getAllData(): LiveData<List<ToDoData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: ToDoData)

    @Update
    suspend fun updateData(toDoData: ToDoData)

    @Delete
    suspend fun deleteItem(toDoData: ToDoData)
}