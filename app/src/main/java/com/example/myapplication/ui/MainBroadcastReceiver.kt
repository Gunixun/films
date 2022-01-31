package com.example.myapplication.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager.EXTRA_NO_CONNECTIVITY
import android.widget.Toast

class MainBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.getBooleanExtra(EXTRA_NO_CONNECTIVITY, false)) {
            StringBuilder().apply {
                Toast.makeText(context, "Невозможно обновить ленту\n", Toast.LENGTH_LONG).show()
            }
        }
    }
}