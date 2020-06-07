package com.brian.weightLess

import android.content.Context
import android.graphics.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToDeleteCallback(private val context: Context) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    private fun getDeleteIcon(): Bitmap {
        val vectorDrawable = (context.getDrawable(R.drawable.ic_delete_white_24dp))!!
        val icon = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(icon)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)

        return icon
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val icon = getDeleteIcon()
            val itemView = viewHolder.itemView
            val height = itemView.bottom.toFloat() - itemView.top.toFloat()
            val width = height / 3
            val p = Paint()

            p.color = Color.parseColor("#D32F2F")

            val background: RectF
            val iconDest: RectF

            if (dX > 0) {
                background = RectF(
                    itemView.left.toFloat(),
                    itemView.top.toFloat(),
                    dX,
                    itemView.bottom.toFloat()
                )
                iconDest = RectF(
                    itemView.left.toFloat() + width,
                    itemView.top.toFloat() + width,
                    itemView.left.toFloat() + 2 * width,
                    itemView.bottom.toFloat() - width
                )
            } else {
                background = RectF(
                    itemView.right.toFloat() + dX,
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat()
                )
                iconDest = RectF(
                    itemView.right.toFloat() - 2 * width,
                    itemView.top.toFloat() + width,
                    itemView.right.toFloat() - width,
                    itemView.bottom.toFloat() - width
                )
            }

            c.drawRect(background, p)
            c.drawBitmap(icon, null, iconDest, p)
        }

        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }
}