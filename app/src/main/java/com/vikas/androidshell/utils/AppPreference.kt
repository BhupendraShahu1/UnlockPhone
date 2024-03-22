package com.vikas.androidshell.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build

object AppPreference {
    private var sharedPreferences: SharedPreferences?=null
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    private const val app: String = "AndroidShell"
    private const val pairedKey: String = "pairedKey"

    fun getInstance(context: Context): AppPreference {
        if (sharedPreferences==null){
            sharedPreferences = context.getSharedPreferences(app, Context.MODE_PRIVATE)
            sharedPreferencesEditor = sharedPreferences!!.edit()
        }

        return AppPreference
    }

    fun saveConnection(key: Boolean) {
        sharedPreferencesEditor.putBoolean(pairedKey, key).commit()
    }

    fun getConnection() = !sharedPreferences!!.getBoolean(pairedKey, false) && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)


}