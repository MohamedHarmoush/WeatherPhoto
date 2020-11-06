package com.harmoush.photoweather.ui.base.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harmoush.photoweather.BR

/**
 * Created by Harmoush on 2020-11-06
 */
abstract class BaseRecyclerViewAdapter<T, VH : BaseViewHolder>(protected val itemsList: MutableList<T> = ArrayList()) :
    RecyclerView.Adapter<VH>() {

    protected var clickListener: OnItemClickListener<T>? = null

    constructor(
        itemsList: MutableList<T> = ArrayList(),
        clickListener: OnItemClickListener<T>?
    ) : this(itemsList) {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        return getItemViewHolder(inflater, parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val currentItem = itemsList[position]
        bindCurrentItem(holder, currentItem, position)
        clickListener.let {
            holder.viewBinding.setVariable(BR.listener, clickListener)
        }
        holder.viewBinding.executePendingBindings()
    }

    abstract fun bindCurrentItem(holder: VH, currentItem: T, position: Int)

    abstract fun getItemViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VH

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun isLastItem(position: Int): Boolean {
        return position == itemCount - 1
    }

    fun updateList(newList: List<T>) {
        itemsList.clear()
        itemsList.addAll(newList)
        notifyDataSetChanged()
    }
}