package com.example.kelolauser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AttendanceAdapter(private var attendanceList: List<Attendance>) : 
    RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    class AttendanceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvInitial: TextView = view.findViewById(R.id.tvInitial)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return AttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val attendance = attendanceList[position]
        holder.tvName.text = attendance.userName
        holder.tvTime.text = "${attendance.date} | ${attendance.time}"
        holder.tvStatus.text = attendance.status
        
        // Tampilkan Inisial
        holder.tvInitial.text = if (attendance.userName.isNotEmpty()) attendance.userName.trim().take(1).uppercase() else "?"
        
        // Warna Status
        val colorRes = if (attendance.status == "Hadir") R.color.tag_hadir_bg else R.color.tag_terlambat_bg
        holder.tvStatus.backgroundTintList = holder.itemView.context.getColorStateList(colorRes)
    }

    override fun getItemCount() = attendanceList.size

    fun updateData(newList: List<Attendance>) {
        attendanceList = newList
        notifyDataSetChanged()
    }
}
