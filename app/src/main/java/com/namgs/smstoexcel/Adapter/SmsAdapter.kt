/*
package com.namgs.smstoexcel.Adapter
import android.telephony.SmsMessage
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class SmsAdapter(private val messages: List<SmsMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENT1 = 1
        private const val VIEW_TYPE_RECEIVED1 = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isSent) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val binding = ItemSmsSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SentViewHolder(binding)
        } else {
            val binding = ItemSmsReceivedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ReceivedViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    class SentViewHolder(private val binding: ItemSmsSentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: SmsMessage) {
            binding.message = message
            binding.executePendingBindings()
        }
    }

    class ReceivedViewHolder(private val binding: ItemSmsReceivedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: SmsMessage) {
            binding.message = message
            binding.executePendingBindings()
        }
    }
}
*/
