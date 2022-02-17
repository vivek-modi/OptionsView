package com.example.optionsview

import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
        return OptionsViewHolder.bindView(parent)
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.root.setOnClickListener {
            itemClickListener.onClickItem(currentItem)
            selectedItemPosition = position
        }
        val drawableColor = if (selectedItemPosition == position)
            R.drawable.options_item_selected_background
        else
            R.drawable.options_item_default_background

        holder.binding.root.background =
            ContextCompat.getDrawable(holder.binding.root.context, drawableColor)
        holder.bindItem(currentItem)
    }

}