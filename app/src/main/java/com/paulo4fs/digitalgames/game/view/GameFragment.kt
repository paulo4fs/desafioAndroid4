package com.paulo4fs.digitalgames.game.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.paulo4fs.digitalgames.R
import com.paulo4fs.digitalgames.utils.Constants
import com.paulo4fs.digitalgames.utils.Constants.CREATED_AT
import com.paulo4fs.digitalgames.utils.Constants.DESCRIPTION
import com.paulo4fs.digitalgames.utils.Constants.IMAGE_URL
import com.paulo4fs.digitalgames.utils.Constants.TITLE
import com.squareup.picasso.Picasso

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
        val imageUrl = arguments?.getString(IMAGE_URL)
        val title = arguments?.getString(TITLE)
        val createdat = arguments?.getInt(CREATED_AT)
        val description = arguments?.getString(DESCRIPTION)

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

        titleView.text = title
        title2View.text = title
        createdAtView.text = createdat.toString()
        descriptionView.text = description
    }

    private fun backButtonListener() {
        val backBtn = _view.findViewById<ImageButton>(R.id.ibBackBtnGame)
        backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}