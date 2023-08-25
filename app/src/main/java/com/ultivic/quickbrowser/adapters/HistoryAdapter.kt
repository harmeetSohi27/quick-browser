package com.ultivic.quickbrowser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ultivic.quickbrowser.R
import com.ultivic.quickbrowser.databinding.ItemHistoryBinding
import com.ultivic.quickbrowser.models.TabHistory
import com.ultivic.quickbrowser.utils.toBitmap
import com.ultivic.quickbrowser.utils.toFormatDate

class HistoryAdapter : ListAdapter<TabHistory, HistoryAdapter.MyViewHolder>(MyDiffUtil) {
    inner class MyViewHolder(val binding: ItemHistoryBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnLongClickListener {
                val position = adapterPosition
                onLongClick?.invoke(getItem(position))
                true
            }
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position == RecyclerView.NO_POSITION) return@setOnClickListener
                onClick?.invoke(getItem(position))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val binding = holder.binding
        with(binding){
            if (getItem(position).icon == null)ivIcon.setImageResource(R.drawable.ic_language) else ivIcon.setImageBitmap(getItem(position).icon?.toBitmap)
            tvUrl.text = getItem(position).originalUrl
            tvTitle.text = getItem(position).title
            tvDate.text = getItem(position).updatedTime.toFormatDate
        }
    }
    var onLongClick:((tabHistory: TabHistory)-> Unit)?=null
    var onClick:((url: TabHistory)-> Unit)?=null
}
val MyDiffUtil = object: DiffUtil.ItemCallback<TabHistory>(){
    override fun areItemsTheSame(oldItem: TabHistory, newItem: TabHistory): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TabHistory, newItem: TabHistory): Boolean {
        return oldItem == newItem
    }

}