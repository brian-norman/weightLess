package com.brian.weightLess

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.brian.weightLess.data.WeightEntity
import com.brian.weightLess.data.getDate
import com.brian.weightLess.databinding.ItemWeightEntryBinding

class WeightAdapter(
    private val clickListener: (WeightEntity) -> Unit
) : RecyclerView.Adapter<WeightAdapter.ViewHolder>() {

    private var data: List<WeightEntity> = emptyList()

    class ViewHolder(val binding: ItemWeightEntryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWeightEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.root.setOnClickListener { clickListener(data[position]) }
        holder.binding.weightTextView.text = "${data[position].pounds} lbs"
        holder.binding.dateTextView.text = data[position].getDate()
    }

    override fun getItemCount(): Int = data.size

    fun setData(newData: List<WeightEntity>) {
        val diffUtil = WeightDiffUtil(data, newData)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        data = newData
        diffResult.dispatchUpdatesTo(this)
    }
}
