package com.cells.library.classes

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class NoFadeItemAnimator : DefaultItemAnimator() {
    companion object {
        private const val ALPHA = 1f
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder?,
        newHolder: RecyclerView.ViewHolder?,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        val value = super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY)
        oldHolder?.itemView?.alpha = ALPHA
        newHolder?.itemView?.alpha = ALPHA
        return value
    }
}