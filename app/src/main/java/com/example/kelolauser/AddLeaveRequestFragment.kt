package com.example.kelolauser

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*

class AddLeaveRequestFragment : Fragment() {

    private lateinit var spinnerLeaveType: Spinner
    private lateinit var etStartDate: EditText
    private lateinit var etEndDate: EditText
    private lateinit var etReason: EditText
    private lateinit var btnSubmit: MaterialButton
    private lateinit var btnViewStatus: TextView

    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_leave_request, container, false)

        spinnerLeaveType = view.findViewById(R.id.spinnerLeaveType)
        etStartDate = view.findViewById(R.id.etStartDate)
        etEndDate = view.findViewById(R.id.etEndDate)
        etReason = view.findViewById(R.id.etReason)
        btnSubmit = view.findViewById(R.id.btnSubmitLeave)
        btnViewStatus = view.findViewById(R.id.btnViewStatus)

        etStartDate.setOnClickListener { showDatePicker { date -> etStartDate.setText(date) } }
        etEndDate.setOnClickListener { showDatePicker { date -> etEndDate.setText(date) } }

        btnSubmit.setOnClickListener { submitRequest() }
        
        btnViewStatus.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
            transaction.replace(R.id.fragment_container, LeaveApprovalFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val format = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
                onDateSelected(format.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun submitRequest() {
        val type = spinnerLeaveType.selectedItem.toString()
        val start = etStartDate.text.toString()
        val end = etEndDate.text.toString()
        val reason = etReason.text.toString()

        if (start.isEmpty() || end.isEmpty() || reason.isEmpty()) {
            Toast.makeText(requireContext(), "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val email = sharedPref.getString("USER_EMAIL", "") ?: ""
        val name = sharedPref.getString("USER_NAME", "Karyawan") ?: "Karyawan"

        Thread {
            val db = AppDatabase.getDatabase(requireContext())
            val request = LeaveRequest(
                userEmail = email,
                userName = name,
                leaveType = type,
                startDate = start,
                endDate = end,
                reason = reason,
                dateSubmitted = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
            )
            db.leaveRequestDao().insert(request)

            activity?.runOnUiThread {
                Toast.makeText(requireContext(), "Pengajuan berhasil dikirim", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
        }.start()
    }
}
