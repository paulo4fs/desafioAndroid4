package com.paulo4fs.digitalgames.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.paulo4fs.digitalgames.R
import com.paulo4fs.digitalgames.addgame.model.GameModel
import com.paulo4fs.digitalgames.home.adapter.HomeAdapter
import com.paulo4fs.digitalgames.home.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var _view: View
    private lateinit var _recyclerView: RecyclerView
    private lateinit var _gameAdapter: HomeAdapter
    private lateinit var _navController: NavController
    private val _homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    private var _gameList = mutableListOf<GameModel>()

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
        _navController = findNavController()

        addRecyclerView()
        initViewModel()
        addNewGameHandler()
    }

    private fun initViewModel() {
        _homeViewModel.getGames()

        _homeViewModel.loading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

        _homeViewModel.error.observe(viewLifecycleOwner, {
            snackBarMessage(it)
        })

        _homeViewModel.stateList.observe(viewLifecycleOwner, {
            _gameList.addAll(it)
        })
    }

    private fun addRecyclerView() {
        _recyclerView = _view.findViewById(R.id.recyclerView)
        val manager = GridLayoutManager(_view.context, 2)

        _gameAdapter = HomeAdapter(_gameList) {
            navigateToGame(it.title)
        }

        _recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = manager
            adapter = _gameAdapter
        }
    }

    private fun navigateToGame(title: String) {
        val bundle = bundleOf("title" to title)
        _navController.navigate(R.id.action_homeFragment_to_gameFragment, bundle)
    }

    private fun addNewGameHandler() {
        val fabAddBtn = _view.findViewById<FloatingActionButton>(R.id.fabAddGameHome)
        fabAddBtn.setOnClickListener {

            _navController.navigate(R.id.action_homeFragment_to_addGameFragment)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        val progressBar = _view.findViewById<ProgressBar>(R.id.pbProgressBarHome)
        when {
            isLoading -> {
                progressBar.visibility = View.VISIBLE
            }
            else -> {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun snackBarMessage(message: String) {
        Snackbar.make(_view, message, Snackbar.LENGTH_LONG).show()
    }
}
