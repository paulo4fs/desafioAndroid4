package com.paulo4fs.digitalgames.utils

import android.net.Uri
import android.view.View
import android.webkit.MimeTypeMap

object ProfileUtils {
    fun getFileExtension(view: View, uri: Uri?): String? {
        return MimeTypeMap
            .getSingleton()
            .getExtensionFromMimeType(view.context.contentResolver.getType(uri!!))
    }
}