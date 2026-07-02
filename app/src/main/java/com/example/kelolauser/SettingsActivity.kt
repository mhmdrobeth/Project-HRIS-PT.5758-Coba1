package com.example.kelolauser

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.util.*

class SettingsActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        etJamMasuk = findViewById(R.id.etJamMasuk)
        etJamPulang = findViewById(R.id.etJamPulang)
        etLatitude = findViewById(R.id.etLatitude)
        etLongitude = findViewById(R.id.etLongitude)
        etRadius = findViewById(R.id.etRadius)
        
        cbSenin = findViewById(R.id.cbSenin)
        cbSelasa = findViewById(R.id.cbSelasa)
        cbRabu = findViewById(R.id.cbRabu)
        cbKamis = findViewById(R.id.cbKamis)
        cbJumat = findViewById(R.id.cbJumat)
        cbSabtu = findViewById(R.id.cbSabtu)
        cbMinggu = findViewById(R.id.cbMinggu)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

        loadSettings()

        etJamMasuk.setOnClickListener { showTimePicker(etJamMasuk) }
        etJamPulang.setOnClickListener { showTimePicker(etJamPulang) }

        findViewById<MaterialButton>(R.id.btnSaveJamMasuk).setOnClickListener { saveSetting("jam_masuk", etJamMasuk.text.toString()) }
        findViewById<MaterialButton>(R.id.btnSaveJamPulang).setOnClickListener { saveSetting("jam_pulang", etJamPulang.text.toString()) }
        findViewById<MaterialButton>(R.id.btnSaveLatitude).setOnClickListener { saveSetting("office_latitude", etLatitude.text.toString()) }
        findViewById<MaterialButton>(R.id.btnSaveLongitude).setOnClickListener { saveSetting("office_longitude", etLongitude.text.toString()) }
        findViewById<MaterialButton>(R.id.btnSaveRadius).setOnClickListener { saveSetting("office_radius", etRadius.text.toString()) }
        
        findViewById<MaterialButton>(R.id.btnSaveHariKerja).setOnClickListener { saveHariKerja() }
    }

    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val time = String.format("%02d:%02d", selectedHour, selectedMinute)
            editText.setText(time)
        }, hour, minute, true)
        timePickerDialog.show()
    }

    private fun loadSettings() {
        val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
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
        val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
        Toast.makeText(this, "Berhasil disimpan", Toast.LENGTH_SHORT).show()
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

        val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putStringSet("hari_kerja", selectedDays)
            apply()
        }
        Toast.makeText(this, "Hari kerja berhasil disimpan", Toast.LENGTH_SHORT).show()
    }
}
