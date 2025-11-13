package com.example.unilaskuri

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unilaskuri.databinding.ItemHistoryBinding

class HistoryAdapter(private val sessions: List<SleepSession>) :
    RecyclerView.Adapter<HistoryAdapter.VH>() {

    inner class VH(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val session = sessions[position]
        val hours = session.totalMinutes / 60
        val mins = session.totalMinutes % 60
        holder.binding.dateText.text = session.date
        holder.binding.sleepText.text = "$hours tuntia $mins minuuttia"
    }

    override fun getItemCount(): Int = sessions.size
}
