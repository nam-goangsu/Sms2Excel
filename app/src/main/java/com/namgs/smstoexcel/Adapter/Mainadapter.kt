package com.namgs.smstoexcel.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namgs.smstoexcel.databinding.PhoneNumber2Binding
import com.namgs.smstoexcel.databinding.PhoneNumber2InputBinding
import com.namgs.smstoexcel.databinding.PhoneNumber2SentBinding
import com.namgs.smstoexcel.vo.SmsDataList_1

class Mainadapter(
    private var smslist: List<SmsDataList_1>/*,
    private val selectSms : MutableList<SmsDataLong>*/
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            holder.bind(list)
        } else if (holder is Sms2_sentViewHolder) {
            holder.bind(list)
        } else if (holder is Sms2_inboxViewHolder) {
            holder.bind(list)
        }

    }

    override fun getItemCount(): Int = smslist.size
    /*val binding = PhoneNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return SmsViewHolder(binding)*/
    /*  val view =
          LayoutInflater.from(parent.context).inflate(R.layout.phone_number1, parent, false)
      return SmsViewHolder(FragmentMainBinding.bind(view))*/


    class Sms2ViewHolder(private val binding: PhoneNumber2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: SmsDataList_1) {
            binding.phonenumber.text = message.address
            //binding.headCheck.isChecked = message.
            //  binding.expandButton.isClickable = message.expand

        }
    }

    class Sms2_sentViewHolder(private val binding: PhoneNumber2SentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: SmsDataList_1) {
            binding.sentTime.text = message.date.toString()
            binding.sentBody.text = message.body
        }
    }

    class Sms2_inboxViewHolder(private val binding: PhoneNumber2InputBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: SmsDataList_1) {
            binding.inputTimestamp.text = message.date.toString()
            binding.inputBody.text = message.body.toString()

        }
    }

}