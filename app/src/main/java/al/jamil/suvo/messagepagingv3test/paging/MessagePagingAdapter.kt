package al.jamil.suvo.messagepagingv3test.paging

import al.jamil.suvo.messagepagingv3test.databinding.MessageItemBinding
import al.jamil.suvo.messagepagingv3test.db.Message
import al.jamil.suvo.messagepagingv3test.noPaging.NonPagingMessageAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DisplayMessageDiffUtilCallBack : DiffUtil.ItemCallback<DisplayMessage>() {
    override fun areItemsTheSame(oldItem: DisplayMessage, newItem: DisplayMessage): Boolean {
        return oldItem.messageId == newItem.messageId
    }

    override fun areContentsTheSame(oldItem: DisplayMessage, newItem: DisplayMessage): Boolean {
        return oldItem.message == newItem.message && oldItem.dateStr == newItem.dateStr
    }

}

class MessagePagingAdapter :
    PagingDataAdapter<DisplayMessage, MessagePagingAdapter.MessageViewHolder>(
        DisplayMessageDiffUtilCallBack()
    ) {


    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val displayMessage: DisplayMessage? = getItem(position)
        if (displayMessage == null) holder.bindMessageLoadingView()
        else holder.bind(displayMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }


    class MessageViewHolder(private val binding: MessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(displayMessage: DisplayMessage) {
            binding.tvMessageId.text = displayMessage.messageId.toString()
            binding.tvTime.text = displayMessage.dateStr
            binding.tvMessage.text = displayMessage.message
        }

        fun bindMessageLoadingView() {
            binding.tvMessage.text = "Loading Single Message Pos @ ${absoluteAdapterPosition}..."
            binding.tvTime.text = ""
            binding.tvMessageId.text = ""
        }
    }
}