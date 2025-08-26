package com.example.routineapp.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

data class RoutineItem(val title: String, val time: String?, val done: Boolean)
data class Exercise(val name: String, val sets: Int, val reps: Int, val doneSets: Int = 0)
data class DayHistory(val date: String, val done: Int, val total: Int)

private const val PREFS = "routine_prefs"
private const val ITEMS = "items"
private const val EXS = "exercises"
private const val HIST = "history"
private const val LAST_DAY = "last_day"

private fun prefs(ctx: Context) = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

fun saveItems(ctx: Context, list: List<RoutineItem>) {
    val arr = JSONArray()
    list.forEach { arr.put(JSONObject().apply {
        put("title", it.title); put("time", it.time); put("done", it.done)
    }) }
    prefs(ctx).edit().putString(ITEMS, arr.toString()).apply()
}
fun loadItems(ctx: Context): List<RoutineItem> {
    val s = prefs(ctx).getString(ITEMS, null) ?: return emptyList()
    val arr = JSONArray(s)
    return (0 until arr.length()).map { i ->
        val o = arr.getJSONObject(i)
        RoutineItem(o.getString("title"), o.optString("time").ifBlank { null }, o.getBoolean("done"))
    }
}

fun saveExercises(ctx: Context, list: List<Exercise>) {
    val arr = JSONArray()
    list.forEach { arr.put(JSONObject().apply {
        put("name", it.name); put("sets", it.sets); put("reps", it.reps); put("doneSets", it.doneSets)
    }) }
    prefs(ctx).edit().putString(EXS, arr.toString()).apply()
}
fun loadExercises(ctx: Context): List<Exercise> {
    val s = prefs(ctx).getString(EXS, null) ?: return emptyList()
    val arr = JSONArray(s)
    return (0 until arr.length()).map { i ->
        val o = arr.getJSONObject(i)
        Exercise(o.getString("name"), o.getInt("sets"), o.getInt("reps"), o.optInt("doneSets"))
    }
}

fun loadHistory(ctx: Context): List<DayHistory> {
    val s = prefs(ctx).getString(HIST, null) ?: return emptyList()
    val arr = JSONArray(s)
    return (0 until arr.length()).map { i ->
        val o = arr.getJSONObject(i)
        DayHistory(o.getString("date"), o.getInt("done"), o.getInt("total"))
    }
}
fun appendHistory(ctx: Context, done: Int, total: Int) {
    val list = loadHistory(ctx).toMutableList()
    list.add(DayHistory(LocalDate.now().toString(), done, total))
    val arr = JSONArray()
    list.takeLast(90).forEach { d -> arr.put(JSONObject().apply {
        put("date", d.date); put("done", d.done); put("total", d.total)
    }) }
    prefs(ctx).edit().putString(HIST, arr.toString()).apply()
}

fun isNewDay(ctx: Context): Boolean {
    val last = prefs(ctx).getString(LAST_DAY, null)
    val today = LocalDate.now().toString()
    return last != today
}
fun markToday(ctx: Context) {
    prefs(ctx).edit().putString(LAST_DAY, LocalDate.now().toString()).apply()
}
