package com.example.kelolauser

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import java.util.*

class SettingsFragment : Fragment() {

    private lateinit var etJamMasuk: EditText
    private lateinit var etJamPulang: EditText
    private lateinit var etLatitude: EditText
    private lateinit var etLongitude: EditText
    private lateinit var etRadius: EditText
    
    private lateinit var cbSenin: CheckBox
    private lateinit var cbSelasa: CheckBox
    private lateinit var cbRabu: CheckBox
    private lateinit var cbKamis: CheckBox
    private lateinit var cbJumat: CheckBox
    private lateinit var cbSabtu: CheckBox
    private lateinit var cbMinggu: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_settings, container, false)

        etJamMasuk = view.findViewById(R.id.etJamMasuk)
        etJamPulang = view.findViewById(R.id.etJamPulang)
        etLatitude = view.findViewById(R.id.etLatitude)
        etLongitude = view.findViewById(R.id.etLongitude)
        etRadius = view.findViewById(R.id.etRadius)
        
        cbSenin = view.findViewById(R.id.cbSenin)
        cbSelasa = view.findViewById(R.id.cbSelasa)
        cbRabu = view.findViewById(R.id.cbRabu)
        cbKamis = view.findViewById(R.id.cbKamis)
        cbJumat = view.findViewById(R.id.cbJumat)
        cbSabtu = view.findViewById(R.id.cbSabtu)
        cbMinggu = view.findViewById(R.id.cbMinggu)

        // Hide back button in fragment
        view.findViewById<ImageView>(R.id.btnBack).visibility = View.GONE

        loadSettings()

        etJamMasuk.setOnClickListener { showTimePicker(etJamMasuk) }
        etJamPulang.setOnClickListener { showTimePicker(etJamPulang) }

        view.findViewById<MaterialButton>(R.id.btnSaveJamMasuk).setOnClickListener { saveSetting("jam_masuk", etJamMasuk.text.toString()) }
        view.findViewById<MaterialButton>(R.id.btnSaveJamPulang).setOnClickListener { saveSetting("jam_pulang", etJamPulang.text.toString()) }
        view.findViewById<MaterialButton>(R.id.btnSaveLatitude).setOnClickListener { saveSetting("office_latitude", etLatitude.text.toString()) }
        view.findViewById<MaterialButton>(R.id.btnSaveLongitude).setOnClickListener { saveSetting("office_longitude", etLongitude.text.toString()) }
        view.findViewById<MaterialButton>(R.id.btnSaveRadius).setOnClickListener { saveSetting("office_radius", etRadius.text.toString()) }
        
        view.findViewById<MaterialButton>(R.id.btnSaveHariKerja).setOnClickListener { saveHariKerja() }
        
        return view
    }

    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            val time = String.format("%02d:%02d", selectedHour, selectedMinute)
            editText.setText(time)
        }, hour, minute, true)
        timePickerDialog.show()
    }

    private fun loadSettings() {
        val sharedPref = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        etJamMasuk.setText(sharedPref.getString("jam_masuk", "08:00"))
        etJamPulang.setText(sharedPref.getString("jam_pulang", "17:00"))
        etLatitude.setText(sharedPref.getString("office_latitude", "-6.977116260995142"))
        etLongitude.setText(sharedPref.getString("office_longitude", "110.45034999660835"))
        etRadius.setText(sharedPref.getString("office_radius", "100"))
        
        val hariKerja = sharedPref.getStringSet("hari_kerja", setOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat")) ?: emptySet()
        cbSenin.isChecked = hariKerja.contains("Senin")
        cbSelasa.isChecked = hariKerja.contains("Selasa")
        cbRabu.isChecked = hariKerja.contains("Rabu")
        cbKamis.isChecked = hariKerja.contains("Kamis")
        cbJumat.isChecked = hariKerja.contains("Jumat")
        cbSabtu.isChecked = hariKerja.contains("Sabtu")
        cbMinggu.isChecked = hariKerja.contains("Minggu")
    }

    private fun saveSetting(key: String, value: String) {
        val sharedPref = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
        Toast.makeText(requireContext(), "Berhasil disimpan", Toast.LENGTH_SHORT).show()
    }

    private fun saveHariKerja() {
        val selectedDays = mutableSetOf<String>()
        if (cbSenin.isChecked) selectedDays.add("Senin")
        if (cbSelasa.isChecked) selectedDays.add("Selasa")
        if (cbRabu.isChecked) selectedDays.add("Rabu")
        if (cbKamis.isChecked) selectedDays.add("Kamis")
        if (cbJumat.isChecked) selectedDays.add("Jumat")
        if (cbSabtu.isChecked) selectedDays.add("Sabtu")
        if (cbMinggu.isChecked) selectedDays.add("Minggu")

        val sharedPref = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putStringSet("hari_kerja", selectedDays)
            apply()
        }
        Toast.makeText(requireContext(), "Hari kerja berhasil disimpan", Toast.LENGTH_SHORT).show()
    }
}
