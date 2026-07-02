package com.example.kelolauser

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EmployeeFragment : Fragment() {

    private lateinit var rvEmployee: RecyclerView
    private lateinit var adapter: EmployeeAdapter
    private lateinit var etSearch: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_employee, container, false)

        rvEmployee = view.findViewById(R.id.rvEmployee)
        etSearch = view.findViewById(R.id.etSearch)
        rvEmployee.layoutManager = LinearLayoutManager(requireContext())

        loadEmployeeData()

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchEmployee(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

    private fun loadEmployeeData() {
        Thread {
            val db = AppDatabase.getDatabase(requireContext())
            val employeeList = db.userDao().getAll()
            
            activity?.runOnUiThread {
                adapter = EmployeeAdapter(employeeList)
                rvEmployee.adapter = adapter
            }
        }.start()
    }

    private fun searchEmployee(query: String) {
        Thread {
            val db = AppDatabase.getDatabase(requireContext())
            val filteredList = db.userDao().searchUsers("%$query%")
            
            activity?.runOnUiThread {
                adapter.updateList(filteredList)
            }
        }.start()
    }
}
