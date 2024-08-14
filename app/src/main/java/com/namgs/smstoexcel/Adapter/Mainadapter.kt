package com.namgs.smstoexcel.Adapter

import android.provider.Telephony
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namgs.smstoexcel.R
import com.namgs.smstoexcel.databinding.FragmentMainBinding
import com.namgs.smstoexcel.databinding.PhoneNumberBinding
import com.namgs.smstoexcel.viewmodel.ShardViewModel
import com.namgs.smstoexcel.vo.SmsDataLong

class Mainadapter(
    private var smslist : List<SmsDataLong>/*,
    private val selectSms : MutableList<SmsDataLong>*/
): RecyclerView.Adapter<Mainadapter.SmsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {

        val binding = PhoneNumberBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  SmsViewHolder(binding)
      /*  val view =
            LayoutInflater.from(parent.context).inflate(R.layout.phone_number1, parent, false)
        return SmsViewHolder(FragmentMainBinding.bind(view))*/
    }

    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        val message = smslist[position]


        holder.bind(message)
    }



    override fun getItemCount(): Int = smslist.size



    inner class SmsViewHolder(private val binding: PhoneNumberBinding) :
        RecyclerView.ViewHolder(binding.root) {
       fun bind(message : SmsDataLong){
           binding.phonenumber.text = message.address
           binding.timeText.text = message.date.toString()
           binding.smsbodyText.text = message.body.toString()

       }


    }
}