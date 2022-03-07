package com.example.optionsview

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class OptionsAdapter(
    private val itemClickListener: ItemClickListener
) : ListAdapter<VariantNode, OptionsViewHolder>(PRODUCT_VARIANT_COMPARATOR) {

    var selectedItemPosition: Int = 0
        set(value) {
            val oldPosition = field
            field = value
            notifyItemChanged(oldPosition)
            notifyItemChanged(value)
        }

    var bestValue = VariantNode()

    companion object {
        private val PRODUCT_VARIANT_COMPARATOR = object : DiffUtil.ItemCallback<VariantNode>() {
            override fun areItemsTheSame(
                oldItem: VariantNode,
                newItem: VariantNode
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: VariantNode,
                newItem: VariantNode
            ): Boolean {
                return oldItem.value == newItem.value
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        val currentItem = currentList[viewType]
        return if (currentItem is StrengthNode) {
            OptionsViewHolder.bindView(parent, currentItem)
        } else {
            OptionsViewHolder.bindView(parent)
        }
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.textContainer.setOnClickListener {
            selectedItemPosition = position
        }

        if (selectedItemPosition == position) {
            itemClickListener.onClickItem(currentItem)
            holder.binding.textContainer.isSelected = true
        } else {
            holder.binding.textContainer.isSelected = false
        }

        holder.bindItem(currentItem, bestValue)
    }

}