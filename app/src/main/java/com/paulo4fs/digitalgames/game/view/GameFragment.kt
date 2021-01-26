package com.paulo4fs.digitalgames.game.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.paulo4fs.digitalgames.R
import com.paulo4fs.digitalgames.utils.Constants.GAME_CREATED_AT
import com.paulo4fs.digitalgames.utils.Constants.GAME_DESCRIPTION
import com.paulo4fs.digitalgames.utils.Constants.GAME_ID
import com.paulo4fs.digitalgames.utils.Constants.GAME_IMAGE_URL
import com.paulo4fs.digitalgames.utils.Constants.GAME_TITLE
import com.squareup.picasso.Picasso
import java.util.*

class GameFragment : Fragment() {
    private lateinit var _view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _view = view

        argumentsHandler()
        backButtonListener()
    }

    private fun argumentsHandler() {
        val id = arguments?.getString(GAME_ID)
        val imageUrl = arguments?.getString(GAME_IMAGE_URL)
        val title = arguments?.getString(GAME_TITLE)
        val createdAt = arguments?.getInt(GAME_CREATED_AT)
        val description = arguments?.getString(GAME_DESCRIPTION)

        val imageCoverView = _view.findViewById<ImageView>(R.id.ivCoverGame)
        val titleView = _view.findViewById<TextView>(R.id.tvTitleGame)
        val title2View = _view.findViewById<TextView>(R.id.tvTitle2Game)
        val createdAtView = _view.findViewById<TextView>(R.id.tvDateGame)
        val descriptionView = _view.findViewById<TextView>(R.id.tvDescriptionGame)

        if (imageUrl.isNullOrEmpty()) {
            Picasso.get().load(R.drawable.placeholder).into(imageCoverView)
        } else {
            Picasso.get().load(imageUrl).fit().centerCrop().into(imageCoverView)
        }

        titleView.text = title?.capitalize(Locale.ROOT)
        title2View.text = title?.capitalize(Locale.ROOT)
        createdAtView.text = createdAt.toString()
        descriptionView.text = description

        editListener(id, imageUrl, title, createdAt, description)
    }

    private fun editListener(
        id: String?,
        imageUrl: String?,
        title: String?,
        createdAt: Int?,
        description: String?
    ) {
        val editFab = _view.findViewById<FloatingActionButton>(R.id.fabEditBtnGame)
        editFab.setOnClickListener {
            if (!title.isNullOrEmpty()) {
                val bundle = bundleOf(
                    GAME_ID to id,
                    GAME_IMAGE_URL to imageUrl,
                    GAME_TITLE to title,
                    GAME_CREATED_AT to createdAt,
                    GAME_DESCRIPTION to description
                )
                val navController = findNavController()
                navController.navigate(R.id.action_gameFragment_to_addGameFragment, bundle)
            }
        }
    }

    private fun backButtonListener() {
        val backBtn = _view.findViewById<ImageButton>(R.id.ibBackBtnGame)
        backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}