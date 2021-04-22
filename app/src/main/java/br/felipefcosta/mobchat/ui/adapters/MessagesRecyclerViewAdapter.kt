package br.felipefcosta.mobchat.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.models.entities.TextMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MessagesRecyclerViewAdapter(private val userId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var itemListener: MessagesRecyclerViewItemListener
    val viewTypeUser: Int = 0
    val viewTypeContact: Int = 1


    var messagesList = listOf<TextMessage>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun setRecyclerViewItemListener(listener: MessagesRecyclerViewItemListener) {
        itemListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == viewTypeUser) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_chat_message_item, parent, false)
            return MessagesRecyclerViewAdapter.ViewHolderUser(view)
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_chat_message_item, parent, false)
        return MessagesRecyclerViewAdapter.ViewHolderContact(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (messagesList[position].senderId == userId) {
            (holder as ViewHolderUser).bindItem(messagesList[position], position)
        } else {
            (holder as ViewHolderContact).bindItem(messagesList[position], position)
        }
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messagesList[position].senderId == userId){
            viewTypeUser
        }else viewTypeContact
    }
    class ViewHolderUser(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(textMessage: TextMessage, position: Int) {

            val itemMessage = itemView.findViewById<TextView>(R.id.messageTextView)
            itemMessage.text = textMessage.message

            val formatter = DateTimeFormatter.ofPattern("HH:mm");
            val dateStr = textMessage.messageDate
            val localDateTime = LocalDateTime.parse(dateStr).format(formatter)

            val itemMessageDate = itemView.findViewById<TextView>(R.id.dateTextView)
            itemMessageDate.text = localDateTime
        }
    }

    class ViewHolderContact(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(textMessage: TextMessage, position: Int) {
            val itemMessage = itemView.findViewById<TextView>(R.id.messageTextView)
            itemMessage.text = textMessage.message

            val formatter = DateTimeFormatter.ofPattern("HH:mm");
            val dateStr = textMessage.messageDate
            val localDateTime = LocalDateTime.parse(dateStr).format(formatter)

            val itemMessageDate = itemView.findViewById<TextView>(R.id.dateTextView)
            itemMessageDate.text = localDateTime

        }
    }
}