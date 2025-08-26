package com.example.routineapp.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.routineapp.data.loadItems

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            loadItems(context).forEach { it.time?.let { t -> scheduleReminder(context, it.title, t) } }
        }
    }
}
