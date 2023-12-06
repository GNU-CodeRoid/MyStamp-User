package com.example.mystamp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Base64


object DataUtil {
    @RequiresApi(Build.VERSION_CODES.O)
    fun imageBitmapFromBytes(byteArrayImage: String): Bitmap {
        // ByteArray를 Bitmap으로 변환
        return BitmapFactory.decodeByteArray(
            Base64.getDecoder().decode(byteArrayImage),
            0,
            Base64.getDecoder().decode(byteArrayImage).size
        )

    }
}