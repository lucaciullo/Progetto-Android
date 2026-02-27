package com.example.hcr0_4.data


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val formatterDate = DateTimeFormatter.ISO_LOCAL_DATE
    private val formatterTime = DateTimeFormatter.ISO_LOCAL_TIME
    private val formatterDateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.format(formatterDate)
    }

    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString, formatterDate)
    }

    @TypeConverter
    fun fromLocalTime(time: LocalTime): String {
        return time.format(formatterTime)
    }

    @TypeConverter
    fun toLocalTime(timeString: String): LocalTime {
        return LocalTime.parse(timeString, formatterTime)
    }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime): String {
        return dateTime.format(formatterDateTime)
    }

    @TypeConverter
    fun toLocalDateTime(dateTimeString: String): LocalDateTime {
        return LocalDateTime.parse(dateTimeString, formatterDateTime)
    }
}