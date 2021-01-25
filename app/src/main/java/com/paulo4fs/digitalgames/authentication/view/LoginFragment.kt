package com.paulo4fs.digitalgames.authentication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.paulo4fs.digitalgames.R
import com.paulo4fs.digitalgames.authentication.viewmodel.AuthViewModel
import com.paulo4fs.digitalgames.utils.AuthUtils.hideKeyboard
import com.paulo4fs.digitalgames.utils.AuthUtils.saveLoginPrefs
import com.paulo4fs.digitalgames.utils.AuthUtils.validateEmailPassword

class LoginFragment : Fragment() {
    private lateinit var _view: View
    private val _authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _view = view
        initViewModel()
        checkUserHandler()
        signUpHandler()
        logInHandler()
    }

    private fun checkUserHandler() {
        _authViewModel.checkUser(_view)
    }

    private fun initViewModel() {
        _authViewModel.loading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

        _authViewModel.error.observe(viewLifecycleOwner, {
            snackBarMessage(it)
        })

        _authViewModel.stateLogin.observe(viewLifecycleOwner, {
            if (it) {
                snackBarMessage("Login successful")
                navToHome()
            }
        })

        _authViewModel.stateRegistered.observe(viewLifecycleOwner, {
            if (it) {
                navToHome()
            }
        })
    }

    private fun navToHome() {
        val navController = findNavController()
        navController.navigate(R.id.action_loginFragment_to_homeFragment)
    }

    private fun showLoading(isLoading: Boolean) {
        val progressBar = _view.findViewById<ProgressBar>(R.id.pbProgressBarLogin)
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

    private fun logInHandler() {
        val loginBtn = _view.findViewById<MaterialButton>(R.id.mbLoginLogin)
        val email = _view.findViewById<TextInputEditText>(R.id.tietEmailLogin)
        val password = _view.findViewById<TextInputEditText>(R.id.tietPasswordLogin)
        val checkBox = _view.findViewById<CheckBox>(R.id.cbCheckBoxLogin)
        loginBtn.setOnClickListener {
            hideKeyboard(_view)

            saveLoginPrefs(requireActivity(), checkBox.isChecked)

            if (validateEmailPassword(email.text.toString(), password.text.toString())) {
                _authViewModel.loginUser(
                    requireActivity(),
                    email.text.toString(),
                    password.text.toString()
                )
            } else {
                snackBarMessage("Fill all the fields")
            }
        }
    }

    private fun signUpHandler() {
        val signupBtn = _view.findViewById<MaterialButton>(R.id.mbSignupLogin)
        signupBtn.setOnClickListener {
            clearText()
            val navController = findNavController()
            navController.navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    private fun clearText() {
        val email = _view.findViewById<TextInputEditText>(R.id.tietEmailLogin)
        val password = _view.findViewById<TextInputEditText>(R.id.tietPasswordLogin)
        email.setText("")
        password.setText("")
    }
}