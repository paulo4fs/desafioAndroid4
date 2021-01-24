package com.paulo4fs.digitalgames.authentication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.paulo4fs.digitalgames.R
import com.paulo4fs.digitalgames.authentication.viewmodel.AuthViewModel
import com.paulo4fs.digitalgames.utils.AuthUtils.hideKeyboard
import com.paulo4fs.digitalgames.utils.AuthUtils.validadeEmailPassword

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
        _authViewModel.checkUser()
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
                snackBarMessage("Login realizado com sucesso")
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
        loginBtn.setOnClickListener {
            hideKeyboard(_view)
            if (validadeEmailPassword(email.text.toString(), password.text.toString())) {
                _authViewModel.loginUser(
                    requireActivity(),
                    email.text.toString(),
                    password.text.toString()
                )
            } else {
                snackBarMessage("preencha os campos corretamente")
            }
        }
    }

    private fun signUpHandler() {
        val signupBtn = _view.findViewById<MaterialButton>(R.id.mbSignupLogin)
        signupBtn.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }
}