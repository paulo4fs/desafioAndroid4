package com.paulo4fs.digitalgames.utils

import android.net.Uri
import android.util.Patterns
import android.view.View
import android.webkit.MimeTypeMap

object GameUtils {
    fun getFileExtension(view: View, uri: Uri?): String? {
        return MimeTypeMap
            .getSingleton()
            .getExtensionFromMimeType(view.context.contentResolver.getType(uri!!))
    }

    fun validateText(title: String?, date: String?, description: String?): Boolean {
        return when {
            title.isNullOrEmpty() || date.isNullOrEmpty() || description.isNullOrEmpty() -> {
                false
            }
            else -> true
        }
    }
}