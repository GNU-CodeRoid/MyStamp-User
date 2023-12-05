package com.example.mystamp

import android.content.Context
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import java.lang.ref.WeakReference

object AppManager {
    private var uid: String? = null
    private var contextRef: WeakReference<Context>? = null
    private val sp: SharedPreferences?
        get() = contextRef?.get()?.getSharedPreferences(
            "UserPrefs",
            ComponentActivity.MODE_PRIVATE
        )

    fun init(context: Context) {
        contextRef = WeakReference(context)
    }

    fun getStartInit(): Boolean? {
        return sp?.getBoolean("init",false)
    }
    fun setUid(uid: String) {
        this.uid = uid
    }

    fun getUid(): String? {
        return uid
    }

}
