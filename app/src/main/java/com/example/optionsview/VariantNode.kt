package com.example.optionsview

import java.util.*

open class VariantNode {
    var value: ProductValue? = null
    var children: MutableList<VariantNode> = arrayListOf()
    val priorityQueue = PriorityQueue(100) { p0: VariantNode, p1: VariantNode ->
        val variantOne = (p0 as SubscriptionNode).productVariant?.pricePerUnit?.value?.toDouble()
        val variantTwo = (p1 as SubscriptionNode).productVariant?.pricePerUnit?.value?.toDouble()
        when {
            (variantOne ?: Double.MAX_VALUE) < (variantTwo ?: Double.MAX_VALUE) -> {
                -1
            }
            (variantOne ?: Double.MAX_VALUE) > (variantTwo ?: Double.MAX_VALUE) -> {
                1
            }
            else -> {
                0
            }
        }
    }
}