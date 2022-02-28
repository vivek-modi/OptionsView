package com.example.optionsview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.optionsview.databinding.StrengthItemLayoutBinding

class OptionsViewHolder(val binding: StrengthItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun bindView(parent: ViewGroup): OptionsViewHolder {
            return OptionsViewHolder(
                StrengthItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    fun bindItem(item: VariantNode, bestValue: VariantNode) {
        binding.variantText.text = item.value?.value
        when (item) {
            is StrengthNode -> {
                binding.variantPrice.visibility = View.VISIBLE
                binding.variantPrice.text = item.pricePerUnit.toString()
                if (item.isPopular) {
                    binding.popularContainer.visibility = View.VISIBLE
                } else {
                    binding.popularContainer.visibility = View.GONE
                }
            }
            is SubscriptionNode -> {
                binding.variantPrice.visibility = View.VISIBLE
                binding.variantPrice.text = item.productVariant?.pricePerUnit?.value.toString()

                if (bestValue == item) {
                    binding.popularContainer.visibility = View.VISIBLE
                } else {
                    binding.popularContainer.visibility = View.GONE
                }
            }
        }
    }
}