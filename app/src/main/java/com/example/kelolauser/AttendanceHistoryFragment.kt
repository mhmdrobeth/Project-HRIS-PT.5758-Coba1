package com.example.kelolauser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AttendanceHistoryFragment : Fragment() {

    private lateinit var rvHistory: RecyclerView
    private lateinit var adapter: AttendanceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_attendance_history, container, false)

        rvHistory = view.findViewById(R.id.rvAttendanceHistory)
        rvHistory.layoutManager = LinearLayoutManager(requireContext())

        loadAllAttendance()

        view.findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun loadAllAttendance() {
        Thread {
            val db = AppDatabase.getDatabase(requireContext())
            val attendanceList = db.attendanceDao().getAllAttendance()
            
            activity?.runOnUiThread {
                adapter = AttendanceAdapter(attendanceList)
                rvHistory.adapter = adapter
            }
        }.start()
    }
}
