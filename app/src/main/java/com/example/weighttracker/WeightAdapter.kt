package com.example.weighttracker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeightAdapter(
    private var data: List<WeightEntry>
) : RecyclerView.Adapter<WeightAdapter.ViewHolder>() {

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weight_entry, parent, false) as TextView
        return ViewHolder(textView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = data[position].weight.toString()
    }

    fun setData(newData: List<WeightEntry>) {
        data = newData
        notifyDataSetChanged()
    }

}
