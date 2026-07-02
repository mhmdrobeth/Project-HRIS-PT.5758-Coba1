package com.example.kelolauser

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ReportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report, container, false)

        view.findViewById<View>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<View>(R.id.cardReportAttendance).setOnClickListener {
            // Menggunakan AnalyticsActivity yang sudah ada untuk laporan kehadiran
            startActivity(Intent(requireContext(), AnalyticsActivity::class.java))
        }

        view.findViewById<View>(R.id.cardReportPayroll).setOnClickListener {
            // Bisa diarahkan ke PayrollActivity atau buat laporan khusus
            startActivity(Intent(requireContext(), PayrollActivity::class.java))
        }

        view.findViewById<View>(R.id.cardReportEmployee).setOnClickListener {
            // Laporan Karyawan -> Daftar Karyawan (EmployeeFragment) via MainActivity tab index 3
            (activity as? MainActivity)?.navigateToTab(3)
        }

        view.findViewById<View>(R.id.cardReportRecruitment).setOnClickListener {
            // Laporan Rekrutmen -> Daftar Kandidat & Status (ApprovalFragment)
            val transaction = parentFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
            transaction.replace(R.id.fragment_container, ApprovalFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }
}
