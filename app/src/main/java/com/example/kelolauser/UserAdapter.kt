package com.example.kelolauser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private var userList: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvInitial: TextView = itemView.findViewById(R.id.tvInitial)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    interface OnUserClickListener {
        fun onUserClick(user: User)
    }

    private var listener: OnUserClickListener? = null

    fun setOnUserClickListener(listener: OnUserClickListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.tvName.text = user.name
        holder.tvTime.text = user.department
        holder.tvStatus.text = user.status
        
        // Tampilkan Inisial di Avatar
        holder.tvInitial.text = if (user.name.isNotEmpty()) user.name.trim().take(1).uppercase() else "?"
        
        holder.itemView.setOnClickListener {
            listener?.onUserClick(user)
        }
        
        // Ubah warna berdasarkan status
        val colorRes = if (user.status == "Aktif" || user.status == "Hadir") {
            R.color.tag_hadir_bg // Pastikan warna ini ada di colors.xml
        } else {
            R.color.tag_terlambat_bg
        }
        
        holder.tvStatus.setBackgroundResource(R.drawable.bg_tag_status)
        holder.tvStatus.backgroundTintList = holder.itemView.context.getColorStateList(colorRes)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateData(newList: List<User>) {
        userList = newList
        notifyDataSetChanged()
    }
}
