package com.brian.weightLess

import androidx.recyclerview.widget.DiffUtil
import com.brian.weightLess.data.WeightEntity

class WeightDiffUtil(
    private val oldList: List<WeightEntity>,
    private val newList: List<WeightEntity>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].date != newList[newItemPosition].date -> false
            oldList[oldItemPosition].pounds != newList[newItemPosition].pounds -> false
            else -> true
        }
    }
}
