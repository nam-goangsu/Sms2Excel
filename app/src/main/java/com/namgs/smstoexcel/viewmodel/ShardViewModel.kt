package com.namgs.smstoexcel.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namgs.smstoexcel.data.loadsms
import com.namgs.smstoexcel.vo.SmsDataLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ShardViewModel: ViewModel() {




     private val _stMessageint = MutableLiveData<Int>()
     val stMessageint: LiveData<Int>  = _stMessageint

     fun setMessageType(radio : Int){
          _stMessageint.value = radio
     }

     // TODO: Implement the ViewModel

     private var _defaltdata = MutableLiveData<List<SmsDataLong>>()
     val defaltdata : LiveData<List<SmsDataLong>> = _defaltdata



     fun loadSmsMessages(loadsms: loadsms,stdate : Long , eddate :Long ,type : Int) {
          viewModelScope.launch(Dispatchers.IO) {
               val messages = loadsms.getloadSms(stdate,eddate,type)
               _defaltdata.postValue(messages)
          }
     }



     private val _selectedDate = MutableLiveData<String>()
     val selectedDate: LiveData<String> = _selectedDate

     private val _stDate = MutableLiveData<String>()
     val stDate: LiveData<String>  = _stDate

     init {
          // ViewModel 초기화 시 오늘 날짜로 설정
          val today = Calendar.getInstance()
          setDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), 1)

          setDate1(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
     }

     fun setDate(year: Int, month: Int, day: Int) {
          val calendar = Calendar.getInstance()
          calendar.set(year, month, day)
          calendar.timeInMillis
          val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
          _stDate.value = format.format(calendar.time)
     }


     fun setDate1(year: Int, month: Int, day: Int) {
          val calendar = Calendar.getInstance()
          calendar.set(year, month, day)
          val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
          _selectedDate.value = format.format(calendar.time)
     }

}