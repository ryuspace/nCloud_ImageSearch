package com.hackday.imageSearch.ml

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class MLActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun getPathOfAllImages(): ArrayList<String>? {
        val result: ArrayList<String> = ArrayList()
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection =
            arrayOf(MediaColumns.DATA, MediaColumns.DISPLAY_NAME)

        val cursor =
            contentResolver.query(uri, projection, null, null, MediaColumns.DATE_ADDED + " desc")

        val columnIndex: Int = cursor?.getColumnIndexOrThrow(MediaColumns.DATA) ?: return result
        while (cursor.moveToNext()) {
            val absolutePathOfImage: String = cursor.getString(columnIndex)
            if (!TextUtils.isEmpty(absolutePathOfImage)) {
                result.add(absolutePathOfImage)
            }
        }
        for (string in result) {
            Log.i("PhotoSelectActivity.java | getPathOfAllImages", "|$string|")
        }
        cursor.close();
        return result
    }
}
