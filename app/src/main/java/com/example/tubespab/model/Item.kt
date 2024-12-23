package com.example.tubespab.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class Item(
    val name: String,
    val expiredDate: String? = "no date",
    val stock: Int = 0,
    val unitType: String? = null,
    val icon: String? = null,
) {
    constructor() : this("", "", 0)

    fun getDaysUntilExpiry(): Int {
        if (expiredDate == "no date" || expiredDate.isNullOrBlank()) return -1 // Jika tidak ada tanggal kadaluarsa

        val format = SimpleDateFormat("d MMMM yyyy", Locale.getDefault()) // Format tanggal yang sesuai
        val expiredDateParsed: Date = format.parse(expiredDate) ?: return -1

        val currentCalendar = Calendar.getInstance()
        val expiredCalendar = Calendar.getInstance()
        expiredCalendar.time = expiredDateParsed

        val diffInMillis = expiredCalendar.timeInMillis - currentCalendar.timeInMillis
        return (diffInMillis / (1000 * 60 * 60 * 24)).toInt() // Mengembalikan selisih dalam hari
    }
}
