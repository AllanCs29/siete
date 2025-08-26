package com.example.routineapp.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

fun scheduleReminder(ctx: Context, title: String, timeHHmm: String) {
    val parts = timeHHmm.split(":")
    if (parts.size != 2) return
    val hour = parts[0].toIntOrNull() ?: return
    val min = parts[1].toIntOrNull() ?: return

    val cal = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, min)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DATE, 1)
    }

    val intent = Intent(ctx, NotificationReceiver::class.java).apply {
        putExtra("title", title)
    }
    val pi = PendingIntent.getBroadcast(
        ctx,
        (title.hashCode() and 0x7FFFFFFF),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
}
