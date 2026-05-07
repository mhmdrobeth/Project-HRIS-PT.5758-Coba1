package com.example.kelolauser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PayrollAdapter(private var payrollList: List<Payroll>) :
    RecyclerView.Adapter<PayrollAdapter.PayrollViewHolder>() {

    class PayrollViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPeriod: TextView = view.findViewById(R.id.tvPayrollPeriod)
        val tvDate: TextView = view.findViewById(R.id.tvDateIssued)
        val tvBasicSalary: TextView = view.findViewById(R.id.tvBasicSalary)
        val tvAllowance: TextView = view.findViewById(R.id.tvAllowance)
        val tvTotalSalary: TextView = view.findViewById(R.id.tvTotalSalary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayrollViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_payroll, parent, false)
        return PayrollViewHolder(view)
    }

    override fun onBindViewHolder(holder: PayrollViewHolder, position: Int) {
        val payroll = payrollList[position]
        holder.tvPeriod.text = "${payroll.month} ${payroll.year}"
        holder.tvDate.text = payroll.dateIssued
        holder.tvBasicSalary.text = payroll.basicSalary
        holder.tvAllowance.text = payroll.allowance
        holder.tvTotalSalary.text = payroll.totalSalary
    }

    override fun getItemCount() = payrollList.size

    fun updateData(newList: List<Payroll>) {
        payrollList = newList
        notifyDataSetChanged()
    }
}
