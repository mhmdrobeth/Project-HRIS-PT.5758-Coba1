package com.example.kelolauser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LeaveApprovalFragment : Fragment() {

    private lateinit var rvApproval: RecyclerView
    private lateinit var adapter: LeaveRequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_approval, container, false)

        view.findViewById<View>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val tvTitle: android.widget.TextView = view.findViewById(R.id.tvApprovalTitle)
        tvTitle.text = "Persetujuan Cuti"

        rvApproval = view.findViewById(R.id.rvApproval)
        rvApproval.layoutManager = LinearLayoutManager(requireContext())

        adapter = LeaveRequestAdapter(emptyList()) { leaveRequest ->
            showLeaveDetail(leaveRequest)
        }
        rvApproval.adapter = adapter

        loadLeaveRequests()

        return view
    }

    private fun loadLeaveRequests() {
        Thread {
            val db = AppDatabase.getDatabase(requireContext())
            val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
            val role = sharedPref.getString("USER_ROLE", "Karyawan")
            val email = sharedPref.getString("USER_EMAIL", "") ?: ""

            val requests = if (role == "Admin HRD") {
                db.leaveRequestDao().getAll()
            } else {
                db.leaveRequestDao().getByUser(email)
            }
            
            activity?.runOnUiThread {
                adapter.updateData(requests)
            }
        }.start()
    }

    private fun showLeaveDetail(leave: LeaveRequest) {
        val sharedPref = requireContext().getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        val role = sharedPref.getString("USER_ROLE", "Karyawan")

        val builder = AlertDialog.Builder(requireContext(), R.style.CustomDialog)
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_leave_detail, null)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val tvName: android.widget.TextView = dialogView.findViewById(R.id.tvDetailName)
        val tvType: android.widget.TextView = dialogView.findViewById(R.id.tvDetailType)
        val tvDate: android.widget.TextView = dialogView.findViewById(R.id.tvDetailDate)
        val tvReason: android.widget.TextView = dialogView.findViewById(R.id.tvDetailReason)
        val btnAccept: com.google.android.material.button.MaterialButton = dialogView.findViewById(R.id.btnAccept)
        val btnReject: com.google.android.material.button.MaterialButton = dialogView.findViewById(R.id.btnReject)

        tvName.text = leave.userName
        tvType.text = leave.leaveType
        tvDate.text = "${leave.startDate} - ${leave.endDate}"
        tvReason.text = leave.reason

        if (role == "Karyawan" || leave.status != "Pending") {
            btnAccept.visibility = View.GONE
            btnReject.visibility = View.GONE
        }

        btnAccept.setOnClickListener {
            updateStatus(leave.id, "Disetujui")
            dialog.dismiss()
        }

        btnReject.setOnClickListener {
            updateStatus(leave.id, "Ditolak")
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateStatus(id: Int, status: String) {
        Thread {
            val db = AppDatabase.getDatabase(requireContext())
            db.leaveRequestDao().updateStatus(id, status)
            activity?.runOnUiThread {
                Toast.makeText(requireContext(), "Status diperbarui ke $status", Toast.LENGTH_SHORT).show()
                loadLeaveRequests()
            }
        }.start()
    }
}
