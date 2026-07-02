package com.example.kelolauser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import java.text.NumberFormat
import java.util.Locale

class PayrollAdapter(private var payrollList: List<Payroll>) :
    RecyclerView.Adapter<PayrollAdapter.PayrollViewHolder>() {

    private fun formatRupiah(number: Long): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(number).replace("Rp", "Rp ")
    }

    class PayrollViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPeriod: TextView = view.findViewById(R.id.tvPayrollPeriod)
        val tvDate: TextView = view.findViewById(R.id.tvDateIssued)
        val tvBasicSalary: TextView = view.findViewById(R.id.tvBasicSalary)
        val tvAllowance: TextView = view.findViewById(R.id.tvAllowance)
        val tvTotalSalary: TextView = view.findViewById(R.id.tvTotalSalary)
        val btnDownload: View = view.findViewById(R.id.btnDownloadItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayrollViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_payroll, parent, false)
        return PayrollViewHolder(view)
    }

    interface OnItemClickListener {
        fun onItemClick(payroll: Payroll)
        fun onDownloadClick(payroll: Payroll)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: PayrollViewHolder, position: Int) {
        val payroll = payrollList[position]
        holder.tvPeriod.text = "${payroll.month} ${payroll.year}"
        holder.tvDate.text = payroll.dateIssued
        holder.tvBasicSalary.text = formatRupiah(payroll.basicSalary)
        holder.tvAllowance.text = formatRupiah(payroll.allowance)
        holder.tvTotalSalary.text = formatRupiah(payroll.totalSalary)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(payroll)
        }

        holder.btnDownload.setOnClickListener {
            listener?.onDownloadClick(payroll)
        }
    }

    override fun getItemCount() = payrollList.size

    fun updateData(newList: List<Payroll>) {
        payrollList = newList
        notifyDataSetChanged()
    }
}
