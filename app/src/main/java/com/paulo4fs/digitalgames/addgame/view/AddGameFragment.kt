package com.paulo4fs.digitalgames.addgame.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.paulo4fs.digitalgames.R
import com.paulo4fs.digitalgames.addgame.model.GameModel
import com.paulo4fs.digitalgames.addgame.viewmodel.AddGameViewModel
import com.paulo4fs.digitalgames.utils.AuthUtils.hideKeyboard
import com.paulo4fs.digitalgames.utils.Constants
import com.paulo4fs.digitalgames.utils.Constants.PICK_IMAGE_REQUEST_CODE
import com.paulo4fs.digitalgames.utils.Constants.READ_STORAGE_PERMISSION_CODE
import com.paulo4fs.digitalgames.utils.GameUtils.validateText
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class AddGameFragment : Fragment() {
    private lateinit var _view: View
    private val _addGameViewModel: AddGameViewModel by lazy {
        ViewModelProvider(this).get(AddGameViewModel::class.java)
    }
    private lateinit var _navController: NavController
    private var _selectedImageUri: Uri? = null
    private var _gameImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _view = view
        _navController = findNavController()

        initViewModel()
        argumentsListener()
        imageListener()
        addGameListener()
    }

    private fun argumentsListener() {
        val imageUrl = arguments?.getString(Constants.GAME_IMAGE_URL)
        val title = arguments?.getString(Constants.GAME_TITLE)
        val createdAt = arguments?.getInt(Constants.GAME_CREATED_AT)
        val description = arguments?.getString(Constants.GAME_DESCRIPTION)

        if (validateText(title, createdAt.toString(), description)) {
            val imageView = _view.findViewById<ImageView>(R.id.ivImageCoverAddGame)
            val titleView = _view.findViewById<TextInputEditText>(R.id.tietNameAddGame)
            val createdAtView = _view.findViewById<TextInputEditText>(R.id.tietCreatedAtAddGame)
            val descriptionView = _view.findViewById<TextInputEditText>(R.id.tietDescriptionAddGame)

            titleView.setText(title!!.capitalize(Locale.ROOT))
            createdAtView.setText(createdAt.toString())
            descriptionView.setText(description!!.capitalize(Locale.ROOT))
            if (!imageUrl.isNullOrEmpty()) {
                _gameImageUrl = imageUrl
                Picasso.get().load(imageUrl).into(imageView)
            }
        }
    }

    private fun initViewModel() {
        _addGameViewModel.loading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

        _addGameViewModel.error.observe(viewLifecycleOwner, {
            snackBarMessage(it)
        })

        _addGameViewModel.stateGameRegistered.observe(viewLifecycleOwner, {
            snackBarMessage("Game added with success")
            navigateHome(it)
        })

        _addGameViewModel.stateGameUpdated.observe(viewLifecycleOwner, {
            snackBarMessage("Game updated with success")
            navigateHome(it)
        })

        _addGameViewModel.stateImage.observe(viewLifecycleOwner, { state ->
            state?.let {
                if (it != "") {
                    _gameImageUrl = it
                }
                addGameHandler()
            }
        })
    }

    private fun addGameHandler() {
        val titleView = _view.findViewById<TextInputEditText>(R.id.tietNameAddGame)
        val createdAtView = _view.findViewById<TextInputEditText>(R.id.tietCreatedAtAddGame)
        val descriptionView = _view.findViewById<TextInputEditText>(R.id.tietDescriptionAddGame)

        val gameId = if (arguments?.getString(Constants.GAME_ID) != null) {
            arguments?.getString(Constants.GAME_ID)
        } else {
            ""
        }

        val game = GameModel(
            gameId!!,
            _gameImageUrl!!,
            titleView.text.toString().toLowerCase(Locale.ROOT),
            createdAtView.text.toString().toInt(),
            descriptionView.text.toString()
        )
        _addGameViewModel.createGame(game)
    }

    private fun imageListener() {
        val imageView = _view.findViewById<ImageView>(R.id.ivImageCameraAddGame)
        imageView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    _view.context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showImagePicker()
            } else {
                checkReadExternalStoragePermission(
                    _view,
                    READ_STORAGE_PERMISSION_CODE
                )
            }
        }
    }

    private fun addGameListener() {
        val confirmBtn = _view.findViewById<MaterialButton>(R.id.mbSaveAddGame)
        val titleView = _view.findViewById<TextInputEditText>(R.id.tietNameAddGame)
        val createdAtView = _view.findViewById<TextInputEditText>(R.id.tietCreatedAtAddGame)
        val descriptionView = _view.findViewById<TextInputEditText>(R.id.tietDescriptionAddGame)

        confirmBtn.setOnClickListener {
            hideKeyboard(_view)
            if (validateText(
                    titleView.text.toString(),
                    createdAtView.text.toString(),
                    descriptionView.text.toString()
                )
            ) {
                _addGameViewModel.updateGamePhoto(
                    _view, _selectedImageUri
                )
            } else {
                snackBarMessage("Fill all the fields")
            }
        }

    }

    private fun showImagePicker() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    private fun checkReadExternalStoragePermission(view: View, requestCode: Int) {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        val result = ContextCompat.checkSelfPermission(view.context, permission)

        if (result != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(permission)

            requestPermissions(
                listPermissionsNeeded.toTypedArray(),
                requestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE
            && grantResults.contains(PackageManager.PERMISSION_GRANTED)
        ) {
            showImagePicker()
        } else {
            snackBarMessage("Permission denied")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE && data!!.data != null) {
            _selectedImageUri = data.data

            val profileImageView = _view.findViewById<ImageView>(R.id.ivImageCoverAddGame)
            Picasso.get().load(_selectedImageUri).fit().centerCrop().into(profileImageView)
        }
    }

    private fun navigateHome(isUpdated: Boolean) {
        if (isUpdated) {
            hideKeyboard(_view)
            Handler(Looper.getMainLooper()).postDelayed({
                _navController.navigate(R.id.action_addGameFragment_to_homeFragment)
            }, 1500)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        val progressBar = _view.findViewById<ProgressBar>(R.id.pbProgressBarAddGame)
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