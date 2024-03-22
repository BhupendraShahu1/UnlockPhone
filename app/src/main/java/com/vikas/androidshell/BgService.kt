package com.vikas.androidshell

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vikas.androidshell.ViewModels.MainActivityViewModel
import com.vikas.androidshell.utils.AppPreference

class BgService : Service() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var appPreference: AppPreference
    override fun onCreate() {
        super.onCreate()
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(MainActivityViewModel::class.java)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        viewModel.startADBServer()
//        viewModel.adb.sendToShellProcess("adb devices")
//        Log.d("G", "onStartCommand: ${ viewModel.adb.sendToShellProcess("adb devices")}")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

