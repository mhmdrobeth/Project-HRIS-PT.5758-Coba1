package com.example.kelolauser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ApprovalFragment : Fragment() {

    private lateinit var rvApproval: RecyclerView
    private lateinit var adapter: CandidateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_approval, container, false)

        view.findViewById<View>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val tvTitle: TextView = view.findViewById(R.id.tvApprovalTitle)
        tvTitle.text = "Data Kandidat Rekrutmen"

        rvApproval = view.findViewById(R.id.rvApproval)
        rvApproval.layoutManager = LinearLayoutManager(requireContext())

        adapter = CandidateAdapter(emptyList()) { candidate ->
            showCandidateDetail(candidate)
        }
        rvApproval.adapter = adapter

        loadCandidates()

        return view
    }

    private fun showCandidateDetail(candidate: Candidate) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.CustomDialog)
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_candidate_detail, null)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val tvName: TextView = dialogView.findViewById(R.id.tvDetailName)
        val tvPosition: TextView = dialogView.findViewById(R.id.tvDetailPosition)
        val tvEmail: TextView = dialogView.findViewById(R.id.tvDetailEmail)
        val tvPhone: TextView = dialogView.findViewById(R.id.tvDetailPhone)
        val tvDate: TextView = dialogView.findViewById(R.id.tvDetailDate)
        val tvInitial: TextView = dialogView.findViewById(R.id.tvDetailInitial)
        val btnViewCv: com.google.android.material.button.MaterialButton = dialogView.findViewById(R.id.btnViewCv)
        val btnAccept: com.google.android.material.button.MaterialButton = dialogView.findViewById(R.id.btnAccept)
        val btnReject: com.google.android.material.button.MaterialButton = dialogView.findViewById(R.id.btnReject)

        tvName.text = candidate.name
        tvPosition.text = candidate.position
        tvEmail.text = candidate.email
        tvPhone.text = candidate.phone
        tvDate.text = candidate.dateApplied
        tvInitial.text = candidate.name.take(1).uppercase()

        if (candidate.status != "Pending") {
            dialogView.findViewById<View>(R.id.layoutActions).visibility = View.GONE
        }

        btnViewCv.setOnClickListener {
            candidate.cvUri?.let { uriString ->
                try {
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                        setDataAndType(android.net.Uri.parse(uriString), "application/pdf")
                        addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    startActivity(intent)
                } catch (e: Exception) {
                    android.widget.Toast.makeText(requireContext(), "Tidak ada aplikasi untuk membuka PDF", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnAccept.setOnClickListener {
            updateStatus(candidate.id, "Diterima")
            dialog.dismiss()
        }

        btnReject.setOnClickListener {
            updateStatus(candidate.id, "Ditolak")
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateStatus(id: Int, status: String) {
        Thread {
            val db = AppDatabase.getDatabase(requireContext())
            db.candidateDao().updateStatus(id, status)
            activity?.runOnUiThread {
                android.widget.Toast.makeText(requireContext(), "Kandidat $status", android.widget.Toast.LENGTH_SHORT).show()
                loadCandidates()
            }
        }.start()
    }

    private fun loadCandidates() {
        Thread {
            val db = AppDatabase.getDatabase(requireContext())
            val candidates = db.candidateDao().getAll()
            
            activity?.runOnUiThread {
                adapter.updateData(candidates)
            }
        }.start()
    }
}
