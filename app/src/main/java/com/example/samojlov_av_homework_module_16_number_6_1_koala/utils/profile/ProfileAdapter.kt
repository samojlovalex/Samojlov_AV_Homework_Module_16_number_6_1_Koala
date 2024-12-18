package com.example.samojlov_av_homework_module_16_number_6_1_koala.utils.profile

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.samojlov_av_homework_module_16_number_6_1_koala.R
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.Profile

class ProfileAdapter(private val context: Context) :
    RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    private val profiles = ArrayList<Profile>()

    private var onProfileClickListener: OnProfileClickListener? = null

    interface OnProfileClickListener {
        fun onProfileClick(profile: Profile, position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Profile>) {
        profiles.clear()
        profiles.addAll(newList)
        notifyDataSetChanged()
    }


    inner class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.profileImageItemIV)
        private val login: TextView = itemView.findViewById(R.id.loginProfileItemTV)

        fun bind(profile: Profile) {
            if (profile.icon != null) {
                val iconUri = profile.icon!!.toUri()
                icon.setImageURI(iconUri)
            } else {
                icon.setImageResource(R.drawable.profile_icon_base)
            }

            login.text = profile.login

            if (profile.selected){
                login.setTextColor(Color.parseColor("#00ff7f"))
            } else{
                login.setTextColor(Color.parseColor("#0000ff"))
            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ProfileViewHolder {
        val viewHolder = ProfileViewHolder(
            LayoutInflater.from(context).inflate(R.layout.profile_item, parent, false)
        )
        return viewHolder
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val currentProfile = profiles[position]
        holder.bind(currentProfile)

        holder.itemView.setOnClickListener {
            if (onProfileClickListener != null) {
                onProfileClickListener!!.onProfileClick(currentProfile, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    fun setOnProfileClickListener(onProfileClickListener: OnProfileClickListener) {
        this.onProfileClickListener = onProfileClickListener
    }

}