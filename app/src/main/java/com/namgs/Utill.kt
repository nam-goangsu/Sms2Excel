package com.namgs

import android.annotation.SuppressLint
import android.view.View
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.app.ActivityCompat.startActivity
import com.google.android.material.snackbar.Snackbar
import com.namgs.smstoexcel.MainActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar

class Utill {

    fun setSnackbar(view : View, message : String,activity: MainActivity ){
        var snack = Snackbar.make(view,message,Snackbar.LENGTH_LONG)
            .apply { this.anchorView = anchorView }
        snack.setAction("App 종료!", View.OnClickListener {
            snack.dismiss()
            finishAffinity(activity)
        })
        var layoutParam = snack.view.layoutParams as FrameLayout.LayoutParams

        snack.view.layoutParams = layoutParam
        snack.show()
    }



    fun mill2Time( date : Long) : LocalDateTime{
        return  Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    fun time2Format(currentDateTime: LocalDateTime):String{
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(currentDateTime)
    }

    fun mill2Format(date :Long) : String{
        return time2Format(mill2Time(date))
    }

    @SuppressLint("SimpleDateFormat")
    fun dateTimeToMillSec(dateTime: String): Long{
        var timeInMilliseconds: Long = 0
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            val mDate = sdf.parse(dateTime)
            timeInMilliseconds = mDate.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return timeInMilliseconds
    }
    fun convertToMillis(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day, hour, minute, second) // month는 0부터 시작하므로 -1 해줘야 함
        return calendar.timeInMillis
    }
    fun convertToMillis(year: Int, month: Int, day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day) // month는 0부터 시작하므로 -1 해줘야 함
        return calendar.timeInMillis
    }
    fun convertToMillis_local(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Long {
        val localDateTime = LocalDateTime.of(year, month, day, hour, minute, second)
        return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
    }
    fun convertToMillis_local(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
        val localDateTime = LocalDateTime.of(year, month, day,hour,minute)
        return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
    }

    fun getMillisto_Calendar(millis: Long): Triple<Int, Int, Int> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작하므로 +1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return Triple(year, month, day)
    }
    fun getMillis_DateTime(millis: Long): Triple<Int, Int, Int> {
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())

        val year = localDateTime.year
        val month = localDateTime.monthValue
        val day = localDateTime.dayOfMonth

        return Triple(year, month, day)
    }


}