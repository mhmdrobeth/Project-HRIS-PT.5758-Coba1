package com.example.kelolauser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class LeaveRequestAdapter(
    private var list: List<LeaveRequest>,
    private val onItemClick: (LeaveRequest) -> Unit
) : RecyclerView.Adapter<LeaveRequestAdapter.ViewHolder>() {

    fun updateData(newList: List<LeaveRequest>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_leave_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvInitial: TextView = view.findViewById(R.id.tvInitial)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvType: TextView = view.findViewById(R.id.tvLeaveType)
        val tvDate: TextView = view.findViewById(R.id.tvDateRange)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val viewInitialBg: View = view.findViewById(R.id.viewInitialBg)

        fun bind(item: LeaveRequest) {
            tvInitial.text = item.userName.take(1).uppercase()
            tvName.text = item.userName
            tvType.text = item.leaveType
            tvDate.text = "${item.startDate} - ${item.endDate}"
            tvStatus.text = item.status

            val (bgColor, textColor) = when (item.status) {
                "Disetujui" -> Pair(R.color.tag_hadir_bg, R.color.white)
                "Ditolak" -> Pair(R.color.action_delete, R.color.white)
                else -> Pair(R.color.tag_terlambat_bg, R.color.white)
            }
            
            tvStatus.setBackgroundResource(R.drawable.bg_rounded_status)
            tvStatus.backgroundTintList = ContextCompat.getColorStateList(itemView.context, bgColor)
            tvStatus.setTextColor(ContextCompat.getColor(itemView.context, textColor))

            // Rebranding Maju Mapan Styling
            val colorPrimary = ContextCompat.getColor(itemView.context, R.color.primary_m2)
            tvName.setTextColor(colorPrimary)
            viewInitialBg.backgroundTintList = ContextCompat.getColorStateList(itemView.context, R.color.circle_blue)
            tvInitial.setTextColor(ContextCompat.getColor(itemView.context, R.color.icon_blue))
        }
    }
}
