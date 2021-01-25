package com.paulo4fs.digitalgames.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
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
import com.paulo4fs.digitalgames.utils.Constants.CREATED_AT
import com.paulo4fs.digitalgames.utils.Constants.DESCRIPTION
import com.paulo4fs.digitalgames.utils.Constants.IMAGE_URL
import com.paulo4fs.digitalgames.utils.Constants.TITLE
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        searchListener()
    }

    private fun initViewModel() {
        _homeViewModel.loading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

        _homeViewModel.error.observe(viewLifecycleOwner, {
            snackBarMessage(it)
        })

        _homeViewModel.stateList.observe(viewLifecycleOwner, {
            Log.d("TAG", "stateList viewmodel")
            addAllgamesHandler(it as MutableList<GameModel>)
        })

        _homeViewModel.getListGames()
    }

    private fun addAllgamesHandler(list: MutableList<GameModel>) {
        Log.d("TAG", "items added to the list")
        _gameList.clear()
        _gameList.addAll(list)
        _gameAdapter.notifyDataSetChanged()
    }

    private fun addRecyclerView() {
        _recyclerView = _view.findViewById(R.id.recyclerView)
        val manager = GridLayoutManager(_view.context, 2)

        _gameAdapter = HomeAdapter(_gameList) {
            navigateToGame(it)
        }

        _recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = manager
            adapter = _gameAdapter
        }
    }

    private fun navigateToGame(gameModel: GameModel) {
        val bundle = bundleOf(
            IMAGE_URL to gameModel.imageUrl,
            TITLE to gameModel.title,
            CREATED_AT to gameModel.createdAt,
            DESCRIPTION to gameModel.description
        )
        _navController.navigate(R.id.action_homeFragment_to_gameFragment, bundle)
    }

    private fun addNewGameHandler() {
        val fabAddBtn = _view.findViewById<FloatingActionButton>(R.id.fabAddGameHome)
        fabAddBtn.setOnClickListener {
            _navController.navigate(R.id.action_homeFragment_to_addGameFragment)
        }
    }

    private fun searchListener() {
        var job: Job? = null
        val searchBar = _view.findViewById<SearchView>(R.id.svSearchViewHome)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    delay(500L)
                    if (query.isNotEmpty()) {
                        _homeViewModel.queryFirebase(query)
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.i("TAG", " Query changed")
                job?.cancel()
                job = MainScope().launch {
                    delay(500L)
                    _homeViewModel.queryFirebase(newText)
                    if (newText.isEmpty()) {
                        _homeViewModel.getListGames()
                    }
                }
                return false
            }
        })
        searchBar.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                _homeViewModel.getListGames()
                return true
            }
        })
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
