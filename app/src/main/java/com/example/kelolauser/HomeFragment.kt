package com.example.kelolauser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.ContextCompat

class HomeFragment : Fragment() {

    private lateinit var attendanceAdapter: AttendanceAdapter
    private lateinit var tvJadwalWaktu: TextView
    private lateinit var tvCurrentDate: TextView
    private lateinit var flexHariKerja: com.google.android.flexbox.FlexboxLayout

    private lateinit var tvDashboardTitle: TextView
    private lateinit var btnPayroll: View
    private lateinit var btnLaporan: View
    private lateinit var btnAnalytics: View
    private lateinit var btnRecruitment: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewUsers)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        attendanceAdapter = AttendanceAdapter(emptyList())
        recyclerView.adapter = attendanceAdapter

        tvDashboardTitle = view.findViewById(R.id.tvDashboardTitle)
        tvJadwalWaktu = view.findViewById(R.id.tvJadwalWaktu)
        tvCurrentDate = view.findViewById(R.id.tvCurrentDate)
        flexHariKerja = view.findViewById(R.id.flexHariKerja)

        btnPayroll = view.findViewById(R.id.btnPayroll)
        btnLaporan = view.findViewById(R.id.btnLaporan)
        btnAnalytics = view.findViewById(R.id.btnAnalytics)
        btnRecruitment = view.findViewById(R.id.btnRecruitment)

        applyRoleSecurity()

        view.findViewById<MaterialButton>(R.id.btnCheckIn).setOnClickListener {
            (activity as? MainActivity)?.navigateToTab(1)
        }

        btnPayroll.setOnClickListener {
            startActivity(Intent(requireContext(), PayrollActivity::class.java))
        }

        btnLaporan.setOnClickListener {
            val sharedPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val role = sharedPref.getString("USER_ROLE", "Karyawan")
            
            if (role == "Karyawan") {
                // Dashboard Karyawan -> Cuti -> Menampilkan Persetujuan Cuti (History & Status)
                val transaction = parentFragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                transaction.replace(R.id.fragment_container, LeaveApprovalFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            } else {
                // Dashboard Admin -> Halaman Laporan (4 Pilihan)
                val transaction = parentFragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                transaction.replace(R.id.fragment_container, ReportFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        btnAnalytics.setOnClickListener {
            val sharedPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val role = sharedPref.getString("USER_ROLE", "Karyawan")
            
            val fragment = if (role == "Karyawan") AddLeaveRequestFragment() else LeaveApprovalFragment()
            
            val transaction = parentFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        btnRecruitment.setOnClickListener {
            startActivity(Intent(requireContext(), RecruitmentActivity::class.java))
        }

        view.findViewById<TextView>(R.id.tvLihatSemua).setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
            transaction.replace(R.id.fragment_container, AttendanceHistoryFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        setCurrentDate()
        return view
    }

    private fun applyRoleSecurity() {
        val sharedPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val role = sharedPref.getString("USER_ROLE", "Karyawan")

        if (role == "Karyawan") {
            tvDashboardTitle.text = "Dashboard Karyawan"
            
            // Sembunyikan fitur khusus Admin & fitur Cuti (karena digantikan Izin)
            btnLaporan.visibility = View.GONE
            btnAnalytics.visibility = View.VISIBLE
            btnRecruitment.visibility = View.GONE
            
            // Ubah label menu untuk Karyawan
            if (btnPayroll is ViewGroup && (btnPayroll as ViewGroup).childCount > 1) {
                val tv = (btnPayroll as ViewGroup).getChildAt(1) as? TextView
                tv?.text = "Slip Gaji"
            }
            
            if (btnAnalytics is ViewGroup && (btnAnalytics as ViewGroup).childCount > 1) {
                val tv = (btnAnalytics as ViewGroup).getChildAt(1) as? TextView
                tv?.text = "Izin"
            }

            // Memastikan icon analytics diganti jadi icon izin/approval
            if (btnAnalytics is ViewGroup && (btnAnalytics as ViewGroup).childCount > 0) {
                val frame = (btnAnalytics as ViewGroup).getChildAt(0) as? ViewGroup
                val img = frame?.getChildAt(1) as? ImageView
                img?.setImageResource(R.drawable.ic_izin)
                img?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.primary_m2))
            }

        } else {
            tvDashboardTitle.text = "Dashboard Admin HRD"
            btnLaporan.visibility = View.VISIBLE
            btnAnalytics.visibility = View.VISIBLE
            btnRecruitment.visibility = View.VISIBLE
            
            if (btnPayroll is ViewGroup && (btnPayroll as ViewGroup).childCount > 1) {
                val tv = (btnPayroll as ViewGroup).getChildAt(1) as? TextView
                tv?.text = "Payroll"
            }
            
            if (btnLaporan is ViewGroup && (btnLaporan as ViewGroup).childCount > 1) {
                val tv = (btnLaporan as ViewGroup).getChildAt(1) as? TextView
                tv?.text = "Laporan"
            }

            if (btnAnalytics is ViewGroup && (btnAnalytics as ViewGroup).childCount > 1) {
                val tv = (btnAnalytics as ViewGroup).getChildAt(1) as? TextView
                tv?.text = "Persetujuan Cuti"
            }

            if (btnRecruitment is ViewGroup && (btnRecruitment as ViewGroup).childCount > 1) {
                val tv = (btnRecruitment as ViewGroup).getChildAt(1) as? TextView
                tv?.text = "Rekrutmen"
            }

            if (btnAnalytics is ViewGroup && (btnAnalytics as ViewGroup).childCount > 0) {
                val frame = (btnAnalytics as ViewGroup).getChildAt(0) as? ViewGroup
                val img = frame?.getChildAt(1) as? ImageView
                img?.setImageResource(R.drawable.ic_izin)
                img?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.primary_m2))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadAttendanceData()
        updateWorkScheduleUI()
    }

    private fun updateWorkScheduleUI() {
        val sharedPref = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val jamMasuk = sharedPref.getString("jam_masuk", "08:00")
        val jamPulang = sharedPref.getString("jam_pulang", "17:00")
        tvJadwalWaktu.text = "$jamMasuk - $jamPulang"

        val hariKerja = sharedPref.getStringSet("hari_kerja", setOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat")) ?: emptySet()
        flexHariKerja.removeAllViews()
        val sortedDays = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
        for (day in sortedDays) {
            if (hariKerja.contains(day)) {
                val textView = TextView(android.view.ContextThemeWrapper(requireContext(), R.style.DayTag), null, 0)
                textView.text = day.take(3)
                flexHariKerja.addView(textView)
            }
        }
    }

    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        tvCurrentDate.text = dateFormat.format(calendar.time)
    }

    private fun loadAttendanceData() {
        Thread {
            val db = AppDatabase.getDatabase(requireContext())
            val attendanceList = db.attendanceDao().getAllAttendance()
            activity?.runOnUiThread {
                // Tampilkan hanya 3 riwayat terbaru
                val limitedList = attendanceList.take(3)
                attendanceAdapter.updateData(limitedList)
                
                // Tombol Lihat Semua selalu ada sesuai perintah
                view?.findViewById<TextView>(R.id.tvLihatSemua)?.visibility = View.VISIBLE
            }
        }.start()
    }
}
