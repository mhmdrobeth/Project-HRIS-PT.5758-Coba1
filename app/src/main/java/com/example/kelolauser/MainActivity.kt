package com.example.kelolauser

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var navHome: LinearLayout
    private lateinit var navAbsensi: LinearLayout
    private lateinit var navApproval: LinearLayout
    private lateinit var navKaryawan: LinearLayout
    private lateinit var navSettings: LinearLayout
    private lateinit var navProfil: LinearLayout

    private var currentNavIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Bottom Nav Views
        navHome = findViewById(R.id.navHome)
        navAbsensi = findViewById(R.id.navAbsensi)
        navApproval = findViewById(R.id.navApproval)
        navKaryawan = findViewById(R.id.navKaryawan)
        navSettings = findViewById(R.id.navSettings)
        navProfil = findViewById(R.id.navProfil)

        // Setup Click Listeners
        navHome.setOnClickListener { switchFragment(HomeFragment(), "Home", navHome, 0) }
        navAbsensi.setOnClickListener { switchFragment(AttendanceFragment(), "Absensi", navAbsensi, 1) }
        navApproval.setOnClickListener { 
            val sharedPref = getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
            val role = sharedPref.getString("USER_ROLE", "Karyawan")
            
            if (role == "Admin HRD") {
                // Admin HRD: Bottom nav tab ke-2 adalah Rekrutmen
                switchFragment(ApprovalFragment(), "Rekrutmen", navApproval, 2)
            } else {
                // Karyawan: Bottom nav tab ke-2 adalah Izin (Cuti) - Sekarang mengarah ke Form Pengajuan
                // sesuai instruksi agar sama dengan fitur Izin di modul aksi cepat dashboard
                switchFragment(AddLeaveRequestFragment(), "Izin", navApproval, 2)
            }
        }
        navKaryawan.setOnClickListener { switchFragment(EmployeeFragment(), "Karyawan", navKaryawan, 3) }
        navSettings.setOnClickListener { switchFragment(SettingsFragment(), "Setelan", navSettings, 4) }
        navProfil.setOnClickListener { switchFragment(ProfileFragment(), "Profil", navProfil, 5) }

        // Set default fragment
        if (savedInstanceState == null) {
            switchFragment(HomeFragment(), "Home", navHome, 0)
        }

        applyRoleAccess()
    }

    private fun applyRoleAccess() {
        val sharedPref = getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        val role = sharedPref.getString("USER_ROLE", "Karyawan")

        if (role == "Karyawan") {
            // Sembunyikan menu yang hanya untuk Admin
            navKaryawan.visibility = android.view.View.GONE
            navSettings.visibility = android.view.View.GONE
            
            // Ubah teks Approval menjadi Izin untuk Karyawan
            val tvApproval = navApproval.getChildAt(1) as? TextView
            tvApproval?.text = "Izin"
        } else {
            // Admin HRD - Tampilkan semua
            navKaryawan.visibility = android.view.View.VISIBLE
            navSettings.visibility = android.view.View.VISIBLE
            
            val tvApproval = navApproval.getChildAt(1) as? TextView
            tvApproval?.text = "Rekrutmen"
        }
    }

    fun navigateToTab(index: Int) {
        when (index) {
            0 -> navHome.performClick()
            1 -> navAbsensi.performClick()
            2 -> navApproval.performClick()
            3 -> navKaryawan.performClick()
            4 -> navSettings.performClick()
            5 -> navProfil.performClick()
        }
    }

    private fun switchFragment(fragment: Fragment, tag: String, activeNav: LinearLayout, index: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        
        // Horizontal sliding animation based on index
        if (index > currentNavIndex) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
        } else if (index < currentNavIndex) {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        
        transaction.replace(R.id.fragment_container, fragment, tag)
        transaction.commit()
        
        currentNavIndex = index
        updateNavUI(activeNav)
    }

    private fun updateNavUI(activeNav: LinearLayout) {
        val navItems = listOf(navHome, navAbsensi, navApproval, navKaryawan, navSettings, navProfil)
        
        for (item in navItems) {
            val icon = item.getChildAt(0) as? ImageView
            val text = item.getChildAt(1) as? TextView
            
            if (item == activeNav) {
                icon?.setColorFilter(ContextCompat.getColor(this, R.color.nav_active))
                text?.setTextColor(ContextCompat.getColor(this, R.color.nav_active))
            } else {
                icon?.setColorFilter(ContextCompat.getColor(this, R.color.nav_inactive))
                text?.setTextColor(ContextCompat.getColor(this, R.color.nav_inactive))
            }
        }
    }
}
