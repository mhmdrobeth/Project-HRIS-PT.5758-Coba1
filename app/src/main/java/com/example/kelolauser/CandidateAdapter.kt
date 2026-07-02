package com.example.kelolauser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CandidateAdapter(
    private var candidates: List<Candidate>,
    private val onItemClick: (Candidate) -> Unit
) : RecyclerView.Adapter<CandidateAdapter.CandidateViewHolder>() {

    class CandidateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvInitial: TextView = view.findViewById(R.id.tvCandidateInitial)
        val tvName: TextView = view.findViewById(R.id.tvCandidateName)
        val tvPosition: TextView = view.findViewById(R.id.tvCandidatePosition)
        val tvDate: TextView = view.findViewById(R.id.tvDateApplied)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_candidate, parent, false)
        return CandidateViewHolder(view)
    }

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        val candidate = candidates[position]
        holder.tvName.text = candidate.name
        holder.tvPosition.text = candidate.position
        holder.tvDate.text = candidate.dateApplied
        holder.tvInitial.text = if (candidate.name.isNotEmpty()) candidate.name.trim().take(1).uppercase() else "?"
        
        holder.tvStatus.text = candidate.status
        when (candidate.status) {
            "Diterima" -> {
                holder.tvStatus.setBackgroundResource(R.drawable.bg_tag_status)
                holder.tvStatus.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#E8F5E9"))
                holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#43A047"))
            }
            "Ditolak" -> {
                holder.tvStatus.setBackgroundResource(R.drawable.bg_tag_status)
                holder.tvStatus.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#FFEDED"))
                holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#E53935"))
            }
            else -> {
                holder.tvStatus.setBackgroundResource(R.drawable.bg_tag_status)
                holder.tvStatus.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#F5F5F5"))
                holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#757575"))
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick(candidate)
        }
    }

    override fun getItemCount() = candidates.size

    fun updateData(newList: List<Candidate>) {
        candidates = newList
        notifyDataSetChanged()
    }
}
