package com.ultivic.quickbrowser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ultivic.quickbrowser.R
import com.ultivic.quickbrowser.databinding.ItemNewTabsBinding
import com.ultivic.quickbrowser.models.TabData
import com.ultivic.quickbrowser.utils.toBitmap

class TabsAdapter : ListAdapter<TabData, TabsAdapter.MyViewHolder>(MyDiffUtils) {
    inner class MyViewHolder(val binding : ItemNewTabsBinding):ViewHolder(binding.root){
        init {
            binding.ivDelete.setOnClickListener {
                val position = adapterPosition
                if (position == RecyclerView.NO_POSITION) return@setOnClickListener
                onDeleteTab?.invoke(getItem(position))
            }
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position == RecyclerView.NO_POSITION) return@setOnClickListener
                onClick?.invoke(getItem(position))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemNewTabsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val binding = holder.binding
        with(binding){
            tvTitle.text = getItem(position).title
            if (getItem(position).screenShot!=null)icImage.setImageBitmap(getItem(position).screenShot?.toBitmap)
            if (getItem(position).icon == null)ivIcon.setImageResource(R.drawable.google) else ivIcon.setImageBitmap(getItem(position).icon?.toBitmap)
        }
    }

    var onDeleteTab : ((tabData: TabData) -> Unit) ?=null
    var onClick : ((TabData) -> Unit) ?=null
}
val MyDiffUtils = object : DiffUtil.ItemCallback<TabData>(){
    override fun areItemsTheSame(oldItem: TabData, newItem: TabData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TabData, newItem: TabData): Boolean {
        return oldItem == newItem
    }

}