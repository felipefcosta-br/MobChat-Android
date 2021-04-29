package br.felipefcosta.mobchat.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.models.entities.Chat
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatListRecyclerViewAdapter(private val userId: String) :
    RecyclerView.Adapter<ChatListRecyclerViewAdapter.ViewHolder>() {

    lateinit var itemListener: ChatListRecyclerViewItemListener

    var chatList = listOf<Chat>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(
            chat: Chat,
            userId: String,
            itemListener: ChatListRecyclerViewItemListener,
            position: Int
        ) {
            val itemImageView = itemView.findViewById<ImageView>(R.id.contactImageChatListImageView)
            itemImageView.clipToOutline = true
            val itemName = itemView.findViewById<TextView>(R.id.contactNameChatListTextView)
            val itemDate = itemView.findViewById<TextView>(R.id.lastMessageDateTextView)
            val itemOfflineMessage =
                itemView.findViewById<TextView>(R.id.offlineMessageCountTextView)
            if (chat.firstMemberId == userId) {
                if (!chat.secondMemberPhoto.isNullOrBlank()) {
                    Picasso.get()
                        .load(chat.secondMemberPhoto)
                        .placeholder(R.drawable.ic_account_circle)
                        .resize(50, 50).centerCrop().into(itemImageView)
                }
                itemName.text = chat.secondMemberName


            } else if (chat.secondMemberId == userId) {
                if (!chat.firstMemberPhoto.isNullOrBlank()) {
                    Picasso.get()
                        .load(chat.firstMemberPhoto)
                        .placeholder(R.drawable.ic_account_circle)
                        .resize(50, 50).centerCrop().into(itemImageView)
                }
                itemName.text = chat.firstMemberName


            }

            val formatter = DateTimeFormatter.ofPattern("HH:mm");
            val dateStr = chat.lastMessageDate
            val localDateTime = LocalDateTime.parse(dateStr).format(formatter)
            itemDate.text = localDateTime.toString()

            if (chat.unreadMessages <= 0) {
                itemOfflineMessage.visibility = View.GONE
            } else {
                itemOfflineMessage.visibility = View.VISIBLE
                itemOfflineMessage.text = chat.unreadMessages.toString()
            }

            itemView.setOnClickListener {
                if (chat.id != null) {
                    itemListener.recyclerViewItemClicked(it, chat)
                }
            }


        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatListRecyclerViewAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_list_item, parent, false)
        return ChatListRecyclerViewAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(chatList[position], userId, itemListener, position)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    fun setRecyclerViewItemListener(listener: ChatListRecyclerViewItemListener) {
        itemListener = listener
    }
}