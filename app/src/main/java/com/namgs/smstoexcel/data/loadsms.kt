package com.namgs.smstoexcel.data

import android.content.Context
import android.database.Cursor
import android.provider.Telephony

import com.namgs.smstoexcel.vo.SmsDataList_1
import com.namgs.smstoexcel.vo.SmsDataLong


class loadsms(private val context: Context) {


    fun getloadSms(stdate: Long, eddate: Long, messageType: Int): List<SmsDataList_1> {
        val smsList = mutableListOf<SmsDataLong>()
        var list: MutableList<SmsDataList_1> = mutableListOf()
        smsList.clear()
        list.clear()


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
                            smsList.add(SmsDataLong(address, body, date, type2))
                        }
                    } else if (messageType == 0) {
                        if (address.length >= 10 && address.length < 12 && subaddres.equals("010")) {

                            val bodyIdx = cursor.getColumnIndex("body")    // 내용
                            val body = cursor.getString(bodyIdx)
                            val date = cursor.getLong(dateIdx)
                            smsList.add(SmsDataLong(address, body, date, type2))
                        }
                    }
                }
            }
            ////////////


            val grouplist = smsList.groupBy { it.address } //[key,map]으로 key 전화번호 map 문자 메시지 정보
            val test2head = grouplist.keys.toMutableList()

            var listinside : SmsDataList_1

            var addresscheck: String = ""
            var viewtype :Int= 0; var addr :String =""; var date:Long=0L; var body : String ="";var expand : Boolean =false;var smstype : Int =0

            test2head.forEachIndexed { index, s ->
                var key = s
                var list_child =grouplist.values.toList()[index]
                list_child.forEachIndexed { index, s ->

                   if (addresscheck.equals("") || !addresscheck.equals(s.address)) {
                        viewtype =-1; addr = key; date=0L; body=""; smstype=-1; expand = false
                        listinside=SmsDataList_1(viewtype,addr,date,body,smstype,expand)
                        list.add(listinside)

                        viewtype =0; addr = key; date=s.date; body=s.body; smstype=s.type; expand = false
                        addresscheck = key
                    }
                    else if (addresscheck.equals(key)) {
                       viewtype =0; addr = key; date=s.date; body=s.body; smstype=s.type; expand = false

                    }
                    else {
                       viewtype =0; addr = key; date=s.date; body=s.body; smstype=s.type; expand = false
                        addresscheck = ""
                    }

                    listinside=SmsDataList_1(viewtype,addr,date,body,smstype,expand)
                    list.add(listinside)

                }
            }
        }  /// return 데이터 형식 변경 및 recyclerview 어댑터 수정 viewtype에 따라 레이아웃 변경 되는 smsadapter 형식으로20240815
        return list
        //return smsList
    }
}