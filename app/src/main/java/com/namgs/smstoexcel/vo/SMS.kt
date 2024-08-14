package com.namgs.smstoexcel.vo

import android.provider.Telephony

data class SMS(
    val tyepo: RowType,
    val address: String,
    val date: String,
    val body: String,
    var checkbox1: Boolean = false
)

data class SMSParent(
    val addresskey: String,
    val smschaild: MutableList<SMS>,
    var mothercheck: Boolean = false,
    var motherexpend: Boolean = false

)

data class SMS_real(val id: String, val address: String, val date: String, val body: String)

enum class RowType(val id: Int) {
    All_Select(0),
    Head_text(1),
    Body_text(2),
    ProductRow(3);

}

enum class messageType (val type : Int){
    inputmessage(Telephony.Sms.MESSAGE_TYPE_INBOX),
    outputmessage(Telephony.Sms.MESSAGE_TYPE_SENT)
}

data class SmsData(val address: String, val body: String, val date: String)

data class SmsDataLong(val address: String, val body: String, val date: Long , val type : Int)

data class SmsData1(val address: String, val body: String, val date: String)

data class selectSMS(val address: String, val date: String, val body: String)


data class selectSMS2(
    val addresskey: String,
    val tyepo: RowType,
    val address: String,
    val date: String,
    val body: String,
    var mothercheck: Boolean = false,
    var motherexpend: Boolean = false
)