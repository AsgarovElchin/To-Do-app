package com.elchinasgarov.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.elchinasgarov.data.models.ToDoData
import com.elchinasgarov.data.viewmodel.SharedViewModel
import com.elchinasgarov.data.viewmodel.ToDoViewModel
import com.elchinasgarov.todoapp.R
import com.elchinasgarov.todoapp.databinding.FragmentListBinding
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


class ListFragment : Fragment(R.layout.fragment_list), android.widget.SearchView.OnQueryTextListener{
    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private lateinit var binding: FragmentListBinding
    private val adapter: ListAdapter = ListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            adapter.submitList(data)
        })

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        mSharedViewModel.emptyDataBase.observe(viewLifecycleOwner, Observer {
            showEmptyDatabaseView(it)
        })


        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider, SearchView.OnQueryTextListener {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu, menu)
                val search = menu.findItem(R.id.menu_search)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.menu_delete_all) {
                    confirmRemoval()
                }
                else if(menuItem.itemId == R.id.menu_priority_high){
                    mToDoViewModel.sortByHighPriority.observe(requireActivity(),Observer{
                        adapter.submitList(it)
                    })
                }
                else if(menuItem.itemId == R.id.menu_priority_low){
                    mToDoViewModel.sortByLowPriority.observe(requireActivity(),Observer{
                        adapter.submitList(it)
                    })
                }

                return true

            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchThroughDatabase(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    searchThroughDatabase(query)
                }
                return true
            }


        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


    }

    private fun restoreDeletedData(view: View, deletedItem: ToDoData, position: Int) {
        val snackbar = Snackbar.make(
            view, "Deleted ${deletedItem.title}", Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo") {
            mToDoViewModel.insertData(deletedItem)
        }
        snackbar.show()

    }

    private fun setUpRecyclerView() {
        binding.listRecyclerView.adapter = adapter
        binding.listRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.listRecyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }
        swipeToDelete(binding.listRecyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallBack = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.currentList[viewHolder.adapterPosition]
                mToDoViewModel.deleteItem(deletedItem)
                restoreDeletedData(viewHolder.itemView, deletedItem, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun showEmptyDatabaseView(emptyDatabase: Boolean) {
        if (emptyDatabase) {
            binding.noDataImageView.visibility = View.VISIBLE
            binding.noDataTextView.visibility = View.VISIBLE
        } else {
            binding.noDataImageView.visibility = View.INVISIBLE
            binding.noDataTextView.visibility = View.INVISIBLE
        }
    }


    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(requireContext(), "Successfully Removed Everything", Toast.LENGTH_SHORT)
                .show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete Everything?")
        builder.setMessage("Are you sure you want to remove Everything?")
        builder.create().show()
    }


    private fun searchThroughDatabase(query: String) {
        var searchQuery = "%${query}%"
        mToDoViewModel.searchDatabase(searchQuery).observe(this, Observer { list ->
            list?.let {
                adapter.submitList(it)
            }

        })

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }


    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }


}

