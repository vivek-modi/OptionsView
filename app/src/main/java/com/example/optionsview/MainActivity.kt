package com.example.optionsview

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.optionsview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<ActivityViewModel>()
    private val strengthItemClickListener = object : ItemClickListener {
        override fun onClickItem(currentItem: VariantNode) {
            if (!viewModel.isFirstTime) {
                supplyAdapter.selectedItemPosition = 0
                subscriptionAdapter.selectedItemPosition = 0
            }
            viewModel.isFirstTime = false
            supplyAdapter.submitList(currentItem.children)
        }
    }
    private val supplyItemClickListener = object : ItemClickListener {
        override fun onClickItem(currentItem: VariantNode) {
            subscriptionAdapter.submitList(currentItem.children)
        }
    }
    private val subscriptionItemClickListener = object : ItemClickListener {
        override fun onClickItem(currentItem: VariantNode) {
            Log.e("currentItem", "${currentItem.value}")
        }
    }
    private val strengthAdapter = OptionsAdapter(strengthItemClickListener)
    private val supplyAdapter = OptionsAdapter(supplyItemClickListener)
    private val subscriptionAdapter = OptionsAdapter(subscriptionItemClickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    private fun setupView() {
        strengthAdapter.selectedItemPosition = viewModel.strengthSearchIndex
        strengthAdapter.submitList(viewModel.baseNode.children)
        binding.strengthRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = strengthAdapter
        }

        supplyAdapter.selectedItemPosition = viewModel.quantitySearchIndex
        binding.supplyRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = supplyAdapter
        }

        subscriptionAdapter.selectedItemPosition = viewModel.subscriptionSearchIndex
        binding.subscriptionRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = subscriptionAdapter
        }
    }
}