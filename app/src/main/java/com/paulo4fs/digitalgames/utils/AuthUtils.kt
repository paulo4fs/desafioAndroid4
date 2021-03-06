package com.paulo4fs.digitalgames.utils

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.paulo4fs.digitalgames.utils.Constants.APP_KEY
import com.paulo4fs.digitalgames.utils.Constants.EMPTY_STRING
import com.paulo4fs.digitalgames.utils.Constants.LOGIN_KEY
import com.paulo4fs.digitalgames.utils.Constants.UIID_KEY

object AuthUtils {
    fun saveUserId(context: Context, uiid: String?) {
        val preferences: SharedPreferences =
            context.getSharedPreferences(APP_KEY, MODE_PRIVATE)
        preferences.edit().putString(UIID_KEY, uiid).apply()
    }

    fun getUserId(context: Context): String? {
        val preferences = context.getSharedPreferences(APP_KEY, MODE_PRIVATE)
        return preferences.getString(UIID_KEY, EMPTY_STRING)
    }

    fun validateEmailPassword(email: String?, password: String?): Boolean {
        return when {
            email.isNullOrEmpty() || password.isNullOrEmpty() -> {
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                false
            }
            password.length < 6 -> {
                false
            }
            else -> true
        }
    }

    fun validateNameEmailPassword(
        name: String?,
        email: String?,
        password: String?,
        confirmationPassword: String?
    ): Boolean {
        return when {
            name.isNullOrEmpty() ||
                    email.isNullOrEmpty() ||
                    password.isNullOrEmpty() ||
                    password != confirmationPassword -> {
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                false
            }
            password.length < 6 -> {
                false
            }
            else -> true
        }
    }

    fun saveLoginPrefs(context: Context, check: Boolean) {
        val preferences: SharedPreferences =
            context.getSharedPreferences(APP_KEY, MODE_PRIVATE)
        preferences.edit().putBoolean(LOGIN_KEY, check).apply()
    }

    fun getLoginPrefs(context: Context): Boolean {
        val preferences: SharedPreferences =
            context.getSharedPreferences(APP_KEY, MODE_PRIVATE)
        return preferences.getBoolean(LOGIN_KEY, false)
    }

    fun hideKeyboard(view: View) {
        val imm: InputMethodManager =
            view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}