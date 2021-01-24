package com.paulo4fs.digitalgames.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.paulo4fs.digitalgames.R

class HomeFragment : Fragment() {
    lateinit var _view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _view = view

        addNewGameHandler()
    }

    private fun addNewGameHandler() {
        val fabAddBtn = _view.findViewById<FloatingActionButton>(R.id.fabAddGameHome)
        fabAddBtn.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_homeFragment_to_addGameFragment)
        }
    }

}
