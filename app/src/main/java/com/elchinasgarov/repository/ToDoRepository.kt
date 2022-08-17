package com.elchinasgarov.repository

import androidx.lifecycle.LiveData
import com.elchinasgarov.data.ToDoDao
import com.elchinasgarov.data.models.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {
    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }
}