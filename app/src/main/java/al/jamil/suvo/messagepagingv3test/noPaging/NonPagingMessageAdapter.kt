package al.jamil.suvo.messagepagingv3test.noPaging

import al.jamil.suvo.messagepagingv3test.databinding.MessageItemBinding
import al.jamil.suvo.messagepagingv3test.db.Message
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NonPagingMessageAdapter : RecyclerView.Adapter<NonPagingMessageAdapter.MessageViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.messageId == newItem.messageId
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.message == newItem.message && oldItem.timestamp == newItem.timestamp
        }

    }

    private val differ = AsyncListDiffer(this, differCallBack)

    fun submitList(list: List<Message>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }


    class MessageViewHolder(private val binding: MessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvMessageId.text = message.messageId.toString()
            val dateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.US)
            binding.tvTime.text = dateFormat.format(Date(message.timestamp))
            binding.tvMessage.text = message.message
        }
    }


}