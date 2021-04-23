package br.felipefcosta.mobchat.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.models.entities.Profile
import com.squareup.picasso.Picasso

class ProfileRecyclerViewAdapter() : RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder>() {

    lateinit var itemListener: SearchProfileRecyclerViewItemListener

    var profileList = listOf<Profile>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindItem(profile: Profile, itemListener: SearchProfileRecyclerViewItemListener, position: Int){

            val itemName = itemView.findViewById<TextView>(R.id.nameProfileTextView)
            itemName.text = "${profile.name} ${profile.surname}"
            val itemUsername = itemView.findViewById<TextView>(R.id.usernameProfileTextView)
            itemUsername.text = profile.userName
            val itemImage = itemView.findViewById<ImageView>(R.id.userProfileImageView)
            Picasso.get().load(profile.photo).resize(50, 50).centerCrop().into(itemImage)

            itemView.setOnClickListener {
                if (profile.id != null){
                    itemListener.recyclerViewItemClicked(it, profile)
                }

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileRecyclerViewAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.profiles_list_item, parent, false)
        return ProfileRecyclerViewAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(profileList[position], itemListener, position)
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    fun setRecyclerViewItemListener(listener: SearchProfileRecyclerViewItemListener) {
        itemListener = listener
    }
}