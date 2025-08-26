package com.example.routineapp.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.routineapp.R
import com.example.routineapp.data.RoutineItem
import com.example.routineapp.data.loadItems
import com.example.routineapp.data.saveItems

class RoutineWidgetProvider : AppWidgetProvider() {

    companion object {
        const val ACTION_MARK_NEXT = "com.example.routineapp.ACTION_MARK_NEXT"
        const val ACTION_REFRESH = "com.example.routineapp.ACTION_REFRESH"
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            ACTION_MARK_NEXT -> {
                val items = loadItems(context).toMutableList()
                val idx = items.indexOfFirst { !it.done }
                if (idx >= 0) {
                    val it = items[idx]
                    items[idx] = RoutineItem(it.title, it.time, true)
                    saveItems(context, items)
                }
                updateAll(context)
            }
            ACTION_REFRESH -> updateAll(context)
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        updateAll(context)
    }

    private fun updateAll(context: Context) {
        val mgr = AppWidgetManager.getInstance(context)
        val thisWidget = ComponentName(context, RoutineWidgetProvider::class.java)
        val ids = mgr.getAppWidgetIds(thisWidget)
        ids.forEach { id ->
            val views = RemoteViews(context.packageName, R.layout.widget_routine)

            val items = loadItems(context)
            val next = items.firstOrNull { !it.done }
            views.setTextViewText(R.id.tvTime, next?.time ?: "—")
            views.setTextViewText(R.id.tvTitle, next?.title ?: "Sin pendientes")

            val markIntent = Intent(context, RoutineWidgetProvider::class.java).setAction(ACTION_MARK_NEXT)
            val markPI = PendingIntent.getBroadcast(context, 1, markIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.btnMark, markPI)

            val refIntent = Intent(context, RoutineWidgetProvider::class.java).setAction(ACTION_REFRESH)
            val refPI = PendingIntent.getBroadcast(context, 2, refIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.btnRefresh, refPI)

            mgr.updateAppWidget(id, views)
        }
    }
}
