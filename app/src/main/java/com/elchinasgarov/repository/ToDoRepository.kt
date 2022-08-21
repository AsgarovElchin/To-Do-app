package com.elchinasgarov.repository

import androidx.lifecycle.LiveData
import com.elchinasgarov.data.ToDoDao
import com.elchinasgarov.data.models.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {
    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }
    suspend fun updateData(todoData: ToDoData){
        toDoDao.updateData(todoData)
    }
    suspend fun deleteItem(toDoData: ToDoData){
        toDoDao.deleteItem(toDoData)
    }
}