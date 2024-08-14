package com.namgs.smstoexcel.data

import android.content.Context
import android.database.Cursor
import android.provider.Telephony
import android.util.Log
import androidx.lifecycle.LiveData
import com.namgs.Utill
import com.namgs.smstoexcel.vo.RowType
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import com.namgs.smstoexcel.vo.SMS
import com.namgs.smstoexcel.vo.SMSParent
import com.namgs.smstoexcel.vo.SmsData
import com.namgs.smstoexcel.vo.SmsData1
import com.namgs.smstoexcel.vo.SmsDataLong
import com.namgs.smstoexcel.vo.messageType
import com.namgs.smstoexcel.vo.selectSMS2

class loadsms(private val context: Context) {


    fun getloadSms(stdate: Long, eddate: Long, messageType: Int): List<SmsDataLong> {
        val smsList = mutableListOf<SmsDataLong>()
        smsList.clear()
        val projection = arrayOf(
            Telephony.Sms._ID,
            Telephony.Sms.ADDRESS,
            Telephony.Sms.BODY,
            Telephony.Sms.TYPE
        )
        val cursor: Cursor? = // sms 정보를 가져옴
            context.contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                null,
                null,
                null,
                null
            )

        cursor?.use {


            val dateIdx = cursor.getColumnIndex("date")    //수신 날짜


            while (it.moveToNext()) {
                val date = it.getLong(it.getColumnIndexOrThrow(Telephony.Sms.DATE)).toLong()

                if (date in stdate..eddate) {

                    val addressIdx = cursor.getColumnIndex("address")  // 전화번호
                    val address = cursor.getString(addressIdx) // 전화번호 확인
                    val subaddres = address.substring(0, 3)
                    val type = cursor.getColumnIndex("type")    // 내용
                    val type2 = cursor.getInt(type)


                    if (type2 == messageType) {

                        if (address.length >= 10 && address.length < 12 && subaddres.equals("010")) {

                            val bodyIdx = cursor.getColumnIndex("body")    // 내용
                            val body = cursor.getString(bodyIdx)
                            val date = cursor.getLong(dateIdx)
                            smsList.add(SmsDataLong(address, body, date, messageType))
                        }
                    }else if(messageType == 0 ){
                        if (address.length >= 10 && address.length < 12 && subaddres.equals("010")) {

                            val bodyIdx = cursor.getColumnIndex("body")    // 내용
                            val body = cursor.getString(bodyIdx)
                            val date = cursor.getLong(dateIdx)
                            smsList.add(SmsDataLong(address, body, date, messageType))
                        }
                    }
                }
            }

        }

        return smsList
    }
}