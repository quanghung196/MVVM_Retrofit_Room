package com.example.mvvm_retrofit_room.view.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T, VB : ViewDataBinding?> :
    RecyclerView.Adapter<BaseViewHolder<VB>>() {
    protected var context: Context? = null
    var data: List<T> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        context = parent.context
        val viewBinding: VB = DataBindingUtil.inflate<VB>(
            LayoutInflater.from(parent.context),
            itemLayout,
            parent,
            false
        )
        return BaseViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        data[position]?.let {
            holder.binding?.run {
                bind(this, it, position)
            }
        }
    }

    abstract val itemLayout: Int

    abstract fun bind(binding: VB, data: T, position: Int)

    override fun getItemCount(): Int {
        return data.size
    }

    fun submitData(data: List<T>) {
        this.data = data
        notifyDataSetChanged()
    }
}