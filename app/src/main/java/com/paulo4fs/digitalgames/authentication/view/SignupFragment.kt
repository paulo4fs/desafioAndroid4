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
import com.paulo4fs.digitalgames.utils.AuthUtils
import kotlin.math.sign


class SignupFragment : Fragment() {
    private lateinit var _view: View
    private val _authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _view = view
        initViewModel()
        signUpHandler()
    }

    private fun initViewModel() {
        _authViewModel.loading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

        _authViewModel.error.observe(viewLifecycleOwner, {
            snackBarMessage(it)
        })

        _authViewModel.stateRegister.observe(viewLifecycleOwner, {
            if (it) {
                snackBarMessage("Conta criada com sucesso")
                navToLogin()
            }
        })
    }

    private fun signUpHandler() {
        val signupBtn = _view.findViewById<MaterialButton>(R.id.mbSignupSignup)
        val nameView = _view.findViewById<TextInputEditText>(R.id.tietNameSignup)
        val emailView = _view.findViewById<TextInputEditText>(R.id.tietEmailSignup)
        val passwordView = _view.findViewById<TextInputEditText>(R.id.tietPasswordSignup)
        val passwordConfirmationView =
            _view.findViewById<TextInputEditText>(R.id.tietPasswordConfirmationSignup)
        signupBtn.setOnClickListener {
            AuthUtils.hideKeyboard(_view)
            when {
                AuthUtils.validadeNameEmailPassword(
                    nameView.text.toString(),
                    emailView.text.toString(),
                    passwordView.text.toString(),
                    passwordConfirmationView.text.toString()
                ) -> {
                    _authViewModel.registerUser(
                        nameView.text.toString(),
                        emailView.text.toString(),
                        passwordView.text.toString(),
                    )
                }
                else -> {
                    snackBarMessage("Validation problem")
                }
            }
        }
    }

    private fun navToLogin() {
        requireActivity().onBackPressed()
    }

    private fun showLoading(isLoading: Boolean) {
        val progressBar = _view.findViewById<ProgressBar>(R.id.pbProgressBarSignup)
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