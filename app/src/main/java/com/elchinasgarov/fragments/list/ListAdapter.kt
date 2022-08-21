package com.elchinasgarov.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.elchinasgarov.data.models.Priority
import com.elchinasgarov.data.models.ToDoData
import com.elchinasgarov.todoapp.R
import com.elchinasgarov.todoapp.databinding.RowLayoutBinding

class ListAdapter : ListAdapter<ToDoData, ListViewHolder>(MainDiffUtils) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowLayoutBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(currentList[position])
        holder.binding.rowBackground.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentList[position])
            holder.binding.root.findNavController().navigate(action)
        }


    }

    object MainDiffUtils : DiffUtil.ItemCallback<ToDoData>() {
        override fun areItemsTheSame(
            oldItem: ToDoData,
            newItem: ToDoData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ToDoData,
            newItem: ToDoData
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }


}