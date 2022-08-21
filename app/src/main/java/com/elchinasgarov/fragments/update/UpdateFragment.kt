package com.elchinasgarov.fragments.update

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.elchinasgarov.data.models.Priority
import com.elchinasgarov.data.viewmodel.SharedViewModel
import com.elchinasgarov.todoapp.R
import com.elchinasgarov.todoapp.databinding.FragmentUpdateBinding


class UpdateFragment : Fragment() {
    private val mSharedViewModel: SharedViewModel by viewModels()
    private lateinit var binding: FragmentUpdateBinding
    private val args by navArgs<UpdateFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.update_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true

            }


        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.currentTitleEditText.setText(args.currentItem.title)
        binding.currentDescriptionEditText.setText(args.currentItem.description)
        binding.currentPrioritiesSpinner.setSelection(parsePriority(args.currentItem.priority))
        binding.currentPrioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener
    }

    private fun parsePriority(priority: Priority): Int {
        return when (priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }
}


