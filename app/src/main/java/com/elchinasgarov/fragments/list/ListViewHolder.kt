package com.elchinasgarov.fragments.list

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.elchinasgarov.data.models.Priority
import com.elchinasgarov.data.models.ToDoData
import com.elchinasgarov.todoapp.R
import com.elchinasgarov.todoapp.databinding.RowLayoutBinding

class ListViewHolder(val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(toDoData: ToDoData) {
        binding.titleTxt.text = toDoData.title
        binding.descriptionTxt.text = toDoData.description
        when (toDoData.priority) {
            Priority.HIGH -> binding.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.red
                )
            )
            Priority.MEDIUM -> binding.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.yellow
                )
            )
            Priority.LOW -> binding.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.green
                )
            )
        }
    }
}