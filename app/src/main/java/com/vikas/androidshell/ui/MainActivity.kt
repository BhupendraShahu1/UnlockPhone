package com.vikas.androidshell.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.vikas.androidshell.BgService
import com.vikas.androidshell.R
import com.vikas.androidshell.ViewModels.MainActivityViewModel
import com.vikas.androidshell.databinding.ActivityMainBinding
import com.vikas.androidshell.utils.AppPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    private lateinit var dataBinding: ActivityMainBinding
    private lateinit var appPreference: AppPreference
    private var lastCommand = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        val intent = Intent(this, BgService()::class.java)
//        startService(intent)
//        mainActivityViewModel.outputData.observe(this, Observer {
//            if (it.equals("Entered adb shell\n" +
//                        "                                                                                                    Success! ※\\(^o^)/※")) {
//                mainActivityViewModel.adb.sendToShellProcess("adb shell input keyevent 82 && adb shell input text 1234 && adb shell input keyevent 66")
//            }
//        })
        appPreference = AppPreference.getInstance(application)

        sendCommandToADB()
        pairAndStart()

        val cmdFragment = CommandFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentLayout, cmdFragment).commit()

        dataBinding.btn.setOnClickListener {
            if (dataBinding.editText.text.trim().isEmpty())
                return@setOnClickListener
            else {
                sendCommandToADB()
            }
        }

    }

    private fun sendCommandToADB() {
        val text = dataBinding.editText.text.trim().toString()
        lastCommand = text
        dataBinding.editText.text = null
        lifecycleScope.launch(Dispatchers.IO) {
            mainActivityViewModel.adb.sendToShellProcess(text)
        }
    }

    private fun pairAndStart() {

        if (appPreference.getConnection()) {
            mainActivityViewModel.adb.debug("Requesting pairing information")
            pairDialogue { thisPairSuccess ->
                if (thisPairSuccess) {
                    appPreference.saveConnection(true)
                    mainActivityViewModel.startADBServer()
                } else {
                    mainActivityViewModel.adb.debug("Failed to pair! Trying again...")
                    runOnUiThread { pairAndStart() }
                }
            }
        } else {
            mainActivityViewModel.startADBServer()
        }
    }

    private fun pairDialogue(onSubmit: (Boolean) -> Unit) {
        val setupPortDialog = Dialog(this)
        setupPortDialog.setContentView(R.layout.input_port)

        val localHostPort = setupPortDialog.findViewById<EditText>(R.id.port)
        val debugSetUpPin = setupPortDialog.findViewById<EditText>(R.id.pin)
        val btnSetUp = setupPortDialog.findViewById<Button>(R.id.btnSetUpConnection)
        btnSetUp.setOnClickListener {
            val portText = localHostPort.text.trim().toString()
            val pinText = debugSetUpPin.text.trim().toString()

            if (portText.isEmpty()) {
                localHostPort.error = "Please enter a port"
                return@setOnClickListener
            }

            if (pinText.isEmpty()) {
                debugSetUpPin.error = "Please enter a pin"
                return@setOnClickListener
            }

            setupPortDialog.dismiss()
            lifecycleScope.launch(Dispatchers.IO) {
                mainActivityViewModel.adb.debug("Trying to pair...")
                val success = mainActivityViewModel.adb.pair(portText, pinText)
                onSubmit.invoke(success)
            }

            setupPortDialog.dismiss()
        }

        setupPortDialog.setCancelable(false)
        setupPortDialog.show()
    }

}