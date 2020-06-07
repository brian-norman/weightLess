package com.brian.weightLess

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.brian.weightLess.data.WeightEntity
import com.brian.weightLess.data.getDate
import kotlinx.android.synthetic.main.item_weight_entry.view.*

class WeightAdapter(
    private var data: List<WeightEntity>,
    private val clickListener: (WeightEntity) -> Unit
) : RecyclerView.Adapter<WeightAdapter.ViewHolder>() {

    class ViewHolder(itemView: CardView) : RecyclerView.ViewHolder(itemView) {
        val weightTextView: TextView = itemView.weightTextView
        val dateTextView: TextView = itemView.dateTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weight_entry, parent, false) as CardView
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener { clickListener(data[position]) }
        holder.weightTextView.text = data[position].weight.toString()
        holder.dateTextView.text = data[position].getDate()
    }

    fun setData(newData: List<WeightEntity>) {
        data = newData
        notifyDataSetChanged()
    }

}
