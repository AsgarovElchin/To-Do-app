package com.elchinasgarov.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.elchinasgarov.data.viewmodel.SharedViewModel
import com.elchinasgarov.data.viewmodel.ToDoViewModel
import com.elchinasgarov.todoapp.R
import com.elchinasgarov.todoapp.databinding.FragmentListBinding
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


class ListFragment : Fragment(R.layout.fragment_list) {
    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel : SharedViewModel by viewModels()
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

        binding.listRecyclerView.adapter = adapter
        binding.listRecyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.listRecyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }

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

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.menu_delete_all) {
                    confirmRemoval()
                }
                return true

            }


        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showEmptyDatabaseView(emptyDatabase:Boolean) {
        if(emptyDatabase){
            binding.noDataImageView.visibility = View.VISIBLE
            binding.noDataTextView.visibility = View.VISIBLE
        }else{
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


}