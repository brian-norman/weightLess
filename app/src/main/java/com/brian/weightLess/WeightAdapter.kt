package com.brian.weightLess

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brian.weightLess.data.WeightEntity
import com.brian.weightLess.data.getDate
import com.brian.weightLess.databinding.ItemWeightEntryBinding

class WeightAdapter(
    private var data: List<WeightEntity>,
    private val clickListener: (WeightEntity) -> Unit
) : RecyclerView.Adapter<WeightAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemWeightEntryBinding) : RecyclerView.ViewHolder(binding.root) {
        val weightTextView: TextView = binding.weightTextView
        val dateTextView: TextView = binding.dateTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWeightEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener { clickListener(data[position]) }
        holder.weightTextView.text = "${data[position].pounds} lbs"
        holder.dateTextView.text = data[position].getDate()
    }

    fun setData(newData: List<WeightEntity>) {
        data = newData
        // TODO This will cause the whole list to re-render, should use DiffUtil
        notifyDataSetChanged()
    }

}
