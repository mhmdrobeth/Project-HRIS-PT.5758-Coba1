package com.example.kelolauser

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import java.io.File

class EmployeeAdapter(private var employees: List<User>) :
    RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    fun updateList(newList: List<User>) {
        employees = newList
        notifyDataSetChanged()
    }

    class EmployeeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvInitial: TextView = view.findViewById(R.id.tvItemInitial)
        val ivProfile: ImageView = view.findViewById(R.id.ivItemProfile)
        val tvName: TextView = view.findViewById(R.id.tvItemName)
        val tvPosition: TextView = view.findViewById(R.id.tvItemPosition)
        val tvStatus: TextView = view.findViewById(R.id.tvItemStatus)
        val cardStatus: MaterialCardView = view.findViewById(R.id.cardItemStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_employee, parent, false)
        return EmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val user = employees[position]
        holder.tvName.text = user.name
        holder.tvPosition.text = "${user.position} - ${user.department}"
        holder.tvStatus.text = user.status.uppercase()
        holder.tvInitial.text = user.initial

        if (user.status.lowercase() == "aktif") {
            holder.cardStatus.setCardBackgroundColor(Color.parseColor("#DCFCE7"))
            holder.tvStatus.setTextColor(Color.parseColor("#166534"))
        } else {
            holder.cardStatus.setCardBackgroundColor(Color.parseColor("#FEE2E2"))
            holder.tvStatus.setTextColor(Color.parseColor("#991B1B"))
        }

        if (!user.profileImage.isNullOrEmpty()) {
            val imgFile = File(user.profileImage)
            if (imgFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                holder.ivProfile.setImageBitmap(bitmap)
                holder.ivProfile.visibility = View.VISIBLE
                holder.tvInitial.visibility = View.GONE
            } else {
                holder.ivProfile.visibility = View.GONE
                holder.tvInitial.visibility = View.VISIBLE
            }
        } else {
            holder.ivProfile.visibility = View.GONE
            holder.tvInitial.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int = employees.size
}
