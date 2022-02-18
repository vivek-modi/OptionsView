package com.example.optionsview

import android.util.Log
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

    fun bindItem(item: VariantNode) {
        binding.variantText.text = item.value?.value
        if (item is StrengthNode) {
            Log.e("item","${item.defaultValue}")
            binding.variantPrice.visibility = View.VISIBLE
            binding.variantPrice.text = item.pricePerUnit
        }
    }
}