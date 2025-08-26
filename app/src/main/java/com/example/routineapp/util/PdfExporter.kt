package com.example.routineapp.util

import android.content.ContentValues
import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.routineapp.data.DayHistory
import com.example.routineapp.data.RoutineItem
import java.io.File
import java.io.FileOutputStream

object PdfExporter {
    fun exportToday(ctx: Context, items: List<RoutineItem>): Boolean {
        val doc = PdfDocument()
        val page = doc.startPage(PdfDocument.PageInfo.Builder(595, 842, 1).create())
        val c = page.canvas
        val p = android.graphics.Paint().apply { textSize = 14f }
        var y = 40f
        c.drawText("Plan de HOY", 40f, y, p); y += 20
        items.forEach { i ->
            c.drawText("${i.time ?: "—"}  ${i.title}   ${if (i.done) "✓" else " "}", 40f, y, p)
            y += 18
        }
        doc.finishPage(page)
        return savePdf(ctx, doc, "Routine_Hoy.pdf")
    }

    fun exportWeekly(ctx: Context, history: List<DayHistory>): Boolean {
        val doc = PdfDocument()
        val page = doc.startPage(PdfDocument.PageInfo.Builder(595, 842, 1).create())
        val c = page.canvas
        val p = android.graphics.Paint().apply { textSize = 14f }
        var y = 40f
        c.drawText("Resumen Semanal", 40f, y, p); y += 20
        history.takeLast(7).forEach {
            c.drawText("${it.date}   ${it.done}/${it.total}", 40f, y, p); y += 18
        }
        doc.finishPage(page)
        return savePdf(ctx, doc, "Routine_Semana.pdf")
    }

    private fun savePdf(ctx: Context, doc: PdfDocument, name: String): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= 29) {
                val values = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, name)
                    put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                }
                val uri = ctx.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                ctx.contentResolver.openOutputStream(uri!!).use { out -> doc.writeTo(out) }
            } else {
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                FileOutputStream(File(dir, name)).use { out -> doc.writeTo(out) }
            }
            doc.close(); true
        } catch (_: Exception) { false }
    }
}
