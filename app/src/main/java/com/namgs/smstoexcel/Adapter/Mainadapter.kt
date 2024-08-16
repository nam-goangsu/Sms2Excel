package com.namgs.smstoexcel.Adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namgs.Utill
import com.namgs.smstoexcel.R
import com.namgs.smstoexcel.databinding.PhoneNumber2Binding
import com.namgs.smstoexcel.databinding.PhoneNumber2InputBinding
import com.namgs.smstoexcel.databinding.PhoneNumber2SentBinding
import com.namgs.smstoexcel.vo.ItemData
import com.namgs.smstoexcel.vo.SMS
import com.namgs.smstoexcel.vo.SmsDataList_1
import java.text.SimpleDateFormat
import java.util.Locale

class Mainadapter(
    private var smslist: List<SmsDataList_1>,
    private val selectSms : MutableList<SmsDataList_1>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val checkboxStatus = SparseBooleanArray()
    //view type 0 number,1 text
    //1 inbox. 2 sent
    companion object {
        private const val MESSAGE_TYPE_SENT = 1
        private const val MESSAGE_TYPE_RECEIVED = 2
        private const val VIEW_TYPE_HEAD = -1
        private const val VIEW_TYPE_BODY = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (smslist[position].viewtype == VIEW_TYPE_HEAD)
            VIEW_TYPE_HEAD
        /*else if (smslist[position].viewtype == VIEW_TYPE_BODY)
            VIEW_TYPE_BODY*/
        else if (smslist[position].smstype == MESSAGE_TYPE_SENT)
            MESSAGE_TYPE_SENT
        else MESSAGE_TYPE_RECEIVED

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEAD) {
            val binding =
                PhoneNumber2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
            Sms2ViewHolder(binding)
        } else if (viewType == MESSAGE_TYPE_SENT) {
            val binding =
                PhoneNumber2SentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            Sms2_sentViewHolder(binding)
        } else {
            val binding =
                PhoneNumber2InputBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            Sms2_inboxViewHolder(binding)

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val list = smslist[position]

        if (holder is Sms2ViewHolder) {
            holder.bind(list,position)
        } else if (holder is Sms2_sentViewHolder) {
            holder.bind(list,position)
        } else if (holder is Sms2_inboxViewHolder) {
            holder.bind(list,position)
        }

    }

    override fun getItemCount(): Int = smslist.size
    /*val binding = PhoneNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return SmsViewHolder(binding)*/
    /*  val view =
          LayoutInflater.from(parent.context).inflate(R.layout.phone_number1, parent, false)
      return SmsViewHolder(FragmentMainBinding.bind(view))*/

    val format1 = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault())

   inner class Sms2ViewHolder(private val binding: PhoneNumber2Binding) : // 헤더류
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: SmsDataList_1, position: Int) {
            binding.phonenumber.text = message.address
            //binding.headCheck.isChecked = message.
            //  binding.expandButton.isClickable = message.expand

            if(message.expand == false){
                binding.expandButton.setImageResource(R.drawable.ic_arrow_drop_down)
            }
            else{
                binding.expandButton.setImageResource(R.drawable.ic_arrow_drop_up)
            }



        }
    }

    inner class Sms2_sentViewHolder(private val binding: PhoneNumber2SentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: SmsDataList_1, position: Int) {

            binding.sentTime.text = Utill().mill2Format(message.date)
            binding.sentBody.text = message.body
            binding.childCheckbox.isChecked = checkboxStatus[position]
            binding.childCheckbox.setOnClickListener{
                if(!binding.childCheckbox.isChecked){
                    selectSms.add(message)
                    checkboxStatus.put(position,false)
                }else{
                    selectSms.add(message)
                    checkboxStatus.put(position,true)
                }
            }
        }
    }

    inner class Sms2_inboxViewHolder(private val binding: PhoneNumber2InputBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: SmsDataList_1, position: Int) {
            binding.inputTimestamp.text = Utill().mill2Format(message.date)
            binding.inputBody.text = message.body.toString()
            binding.childCheckbox.isChecked = checkboxStatus[position]
            binding.childCheckbox.setOnClickListener{
                if(!binding.childCheckbox.isChecked){
                    selectSms.add(message)
                    checkboxStatus.put(position,false)
                }else{
                    selectSms.add(message)
                    checkboxStatus.put(position,true)
                }
            }

        }
    }

}