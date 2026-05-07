package com.example.kelolauser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CandidateAdapter(private var candidates: List<Candidate>) :
    RecyclerView.Adapter<CandidateAdapter.CandidateViewHolder>() {

    class CandidateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvCandidateName)
        val tvPosition: TextView = view.findViewById(R.id.tvCandidatePosition)
        val tvDate: TextView = view.findViewById(R.id.tvDateApplied)
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
    }

    override fun getItemCount() = candidates.size

    fun updateData(newList: List<Candidate>) {
        candidates = newList
        notifyDataSetChanged()
    }
}
