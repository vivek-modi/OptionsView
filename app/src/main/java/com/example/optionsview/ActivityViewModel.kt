package com.example.optionsview

import androidx.lifecycle.ViewModel

class ActivityViewModel : ViewModel() {

    var baseNode: VariantNode = VariantNode()

    init {
        createGraph()
    }

    private fun createGraph() {
        val tempHashMap: MutableMap<String, VariantNode> = mutableMapOf()
        val getUnSortedDataList = getUnSortedDataList()
        val sortedList = getUnSortedDataList.sortedWith(
            compareBy<ProductVariant> {   // or compareByDescending
                it.strength?.value?.toInt() ?: 0   // or java.lang.Integer.MAX_VALUE
            }.thenBy {                    // or thenByDescending
                it.quantity?.value?.toInt() ?: 0   // or java.lang.Integer.MAX_VALUE
            }
        )

        sortedList.forEach { productVariant ->
            productVariant.strength?.let { strength ->
                if (tempHashMap.containsKey("strength_${strength.value}")) {
                    return@let
                }
                val tempNode = StrengthNode().apply {
                    value = strength
                }
                baseNode.children.add(tempNode)
                tempHashMap["strength_${strength.value}"] = tempNode
            }
            productVariant.quantity?.let { quantity ->
                if (tempHashMap.containsKey("strength_${productVariant.strength?.value}_quantity_${quantity.value}")) {
                    return@let
                }
                val tempNode = QuantityNode().apply {
                    value = quantity
                }
                val parent =
                    tempHashMap["strength_${productVariant.strength?.value}"] ?: baseNode
                parent.children.add(tempNode)

                tempHashMap["strength_${productVariant.strength?.value}_quantity_${quantity.value}"] =
                    tempNode
            }
            productVariant.subscription?.let { subscription ->
                val tempNode = SubscriptionNode().apply {
                    value = subscription
                }
                val parent =
                    tempHashMap["strength_${productVariant.strength?.value}_quantity_${productVariant.quantity?.value}"]
                        ?: baseNode
                parent.children.add(tempNode)
            }
        }
        baseNode
    }

    private fun getUnSortedDataList(): List<ProductVariant> {
        return listOf(
            ProductVariant(ProductValue("75"), ProductValue("14"), ProductValue("1")),
            ProductVariant(ProductValue("75"), ProductValue("14"), ProductValue("3")),
            ProductVariant(ProductValue("75"), ProductValue("14"), ProductValue("6")),
            ProductVariant(ProductValue("75"), ProductValue("14"), ProductValue("9")),
            ProductVariant(ProductValue("75"), ProductValue("14"), ProductValue("12")),
            ProductVariant(ProductValue("25"), ProductValue("8"), ProductValue("1")),
            ProductVariant(ProductValue("25"), ProductValue("8"), ProductValue("3")),
            ProductVariant(ProductValue("25"), ProductValue("8"), ProductValue("6")),
            ProductVariant(ProductValue("25"), ProductValue("8"), ProductValue("9")),
            ProductVariant(ProductValue("25"), ProductValue("8"), ProductValue("12")),
            ProductVariant(ProductValue("50"), ProductValue("14"), ProductValue("1")),
            ProductVariant(ProductValue("50"), ProductValue("14"), ProductValue("3")),
            ProductVariant(ProductValue("50"), ProductValue("14"), ProductValue("6")),
            ProductVariant(ProductValue("50"), ProductValue("14"), ProductValue("9")),
            ProductVariant(ProductValue("50"), ProductValue("14"), ProductValue("12")),
            ProductVariant(ProductValue("75"), ProductValue("10"), ProductValue("1")),
            ProductVariant(ProductValue("75"), ProductValue("10"), ProductValue("3")),
            ProductVariant(ProductValue("75"), ProductValue("10"), ProductValue("6")),
            ProductVariant(ProductValue("75"), ProductValue("10"), ProductValue("9")),
            ProductVariant(ProductValue("75"), ProductValue("10"), ProductValue("12")),
            ProductVariant(ProductValue("25"), ProductValue("2"), ProductValue("1")),
            ProductVariant(ProductValue("25"), ProductValue("2"), ProductValue("3")),
            ProductVariant(ProductValue("25"), ProductValue("2"), ProductValue("6")),
            ProductVariant(ProductValue("25"), ProductValue("2"), ProductValue("9")),
            ProductVariant(ProductValue("25"), ProductValue("2"), ProductValue("12")),
            ProductVariant(ProductValue("25"), ProductValue("4"), ProductValue("1")),
            ProductVariant(ProductValue("25"), ProductValue("4"), ProductValue("3")),
            ProductVariant(ProductValue("25"), ProductValue("4"), ProductValue("6")),
            ProductVariant(ProductValue("25"), ProductValue("4"), ProductValue("9")),
            ProductVariant(ProductValue("25"), ProductValue("4"), ProductValue("12")),
            ProductVariant(ProductValue("25"), ProductValue("6"), ProductValue("1")),
            ProductVariant(ProductValue("25"), ProductValue("6"), ProductValue("3")),
            ProductVariant(ProductValue("25"), ProductValue("6"), ProductValue("6")),
            ProductVariant(ProductValue("25"), ProductValue("6"), ProductValue("9")),
            ProductVariant(ProductValue("25"), ProductValue("6"), ProductValue("12")),
            ProductVariant(ProductValue("25"), ProductValue("10"), ProductValue("1")),
            ProductVariant(ProductValue("25"), ProductValue("10"), ProductValue("3")),
            ProductVariant(ProductValue("25"), ProductValue("10"), ProductValue("6")),
            ProductVariant(ProductValue("25"), ProductValue("10"), ProductValue("9")),
            ProductVariant(ProductValue("25"), ProductValue("10"), ProductValue("12")),
            ProductVariant(ProductValue("25"), ProductValue("12"), ProductValue("1")),
            ProductVariant(ProductValue("25"), ProductValue("12"), ProductValue("3")),
            ProductVariant(ProductValue("25"), ProductValue("12"), ProductValue("6")),
            ProductVariant(ProductValue("25"), ProductValue("12"), ProductValue("9")),
            ProductVariant(ProductValue("25"), ProductValue("12"), ProductValue("12")),
            ProductVariant(ProductValue("25"), ProductValue("14"), ProductValue("1")),
            ProductVariant(ProductValue("25"), ProductValue("14"), ProductValue("3")),
            ProductVariant(ProductValue("25"), ProductValue("14"), ProductValue("6")),
            ProductVariant(ProductValue("25"), ProductValue("14"), ProductValue("9")),
            ProductVariant(ProductValue("25"), ProductValue("14"), ProductValue("12")),
            ProductVariant(ProductValue("50"), ProductValue("2"), ProductValue("1")),
            ProductVariant(ProductValue("50"), ProductValue("2"), ProductValue("3")),
            ProductVariant(ProductValue("50"), ProductValue("2"), ProductValue("6")),
            ProductVariant(ProductValue("50"), ProductValue("2"), ProductValue("9")),
            ProductVariant(ProductValue("50"), ProductValue("2"), ProductValue("12")),
            ProductVariant(ProductValue("50"), ProductValue("4"), ProductValue("1")),
            ProductVariant(ProductValue("50"), ProductValue("4"), ProductValue("3")),
            ProductVariant(ProductValue("50"), ProductValue("4"), ProductValue("6")),
            ProductVariant(ProductValue("50"), ProductValue("4"), ProductValue("9")),
            ProductVariant(ProductValue("50"), ProductValue("4"), ProductValue("12")),
            ProductVariant(ProductValue("50"), ProductValue("6"), ProductValue("1")),
            ProductVariant(ProductValue("50"), ProductValue("6"), ProductValue("3")),
            ProductVariant(ProductValue("50"), ProductValue("6"), ProductValue("6")),
            ProductVariant(ProductValue("50"), ProductValue("6"), ProductValue("9")),
            ProductVariant(ProductValue("50"), ProductValue("6"), ProductValue("12")),
            ProductVariant(ProductValue("50"), ProductValue("12"), ProductValue("1")),
            ProductVariant(ProductValue("50"), ProductValue("12"), ProductValue("3")),
            ProductVariant(ProductValue("50"), ProductValue("12"), ProductValue("6")),
            ProductVariant(ProductValue("50"), ProductValue("12"), ProductValue("9")),
            ProductVariant(ProductValue("50"), ProductValue("12"), ProductValue("12")),
            ProductVariant(ProductValue("75"), ProductValue("2"), ProductValue("1")),
            ProductVariant(ProductValue("75"), ProductValue("2"), ProductValue("3")),
            ProductVariant(ProductValue("75"), ProductValue("2"), ProductValue("6")),
            ProductVariant(ProductValue("75"), ProductValue("2"), ProductValue("9")),
            ProductVariant(ProductValue("75"), ProductValue("2"), ProductValue("12")),
            ProductVariant(ProductValue("75"), ProductValue("6"), ProductValue("1")),
            ProductVariant(ProductValue("75"), ProductValue("6"), ProductValue("3")),
            ProductVariant(ProductValue("75"), ProductValue("6"), ProductValue("6")),
            ProductVariant(ProductValue("75"), ProductValue("6"), ProductValue("9")),
            ProductVariant(ProductValue("75"), ProductValue("6"), ProductValue("12")),
            ProductVariant(ProductValue("75"), ProductValue("8"), ProductValue("1")),
            ProductVariant(ProductValue("75"), ProductValue("8"), ProductValue("3")),
            ProductVariant(ProductValue("75"), ProductValue("8"), ProductValue("6")),
            ProductVariant(ProductValue("75"), ProductValue("8"), ProductValue("9")),
            ProductVariant(ProductValue("75"), ProductValue("8"), ProductValue("12")),
            ProductVariant(ProductValue("75"), ProductValue("12"), ProductValue("1")),
            ProductVariant(ProductValue("75"), ProductValue("12"), ProductValue("3")),
            ProductVariant(ProductValue("75"), ProductValue("12"), ProductValue("6")),
            ProductVariant(ProductValue("75"), ProductValue("12"), ProductValue("9")),
            ProductVariant(ProductValue("75"), ProductValue("12"), ProductValue("12")),
            ProductVariant(ProductValue("50"), ProductValue("8"), ProductValue("1")),
            ProductVariant(ProductValue("50"), ProductValue("8"), ProductValue("3")),
            ProductVariant(ProductValue("50"), ProductValue("8"), ProductValue("6")),
            ProductVariant(ProductValue("50"), ProductValue("8"), ProductValue("9")),
            ProductVariant(ProductValue("50"), ProductValue("8"), ProductValue("12")),
            ProductVariant(ProductValue("75"), ProductValue("4"), ProductValue("1")),
            ProductVariant(ProductValue("75"), ProductValue("4"), ProductValue("3")),
            ProductVariant(ProductValue("75"), ProductValue("4"), ProductValue("6")),
            ProductVariant(ProductValue("75"), ProductValue("4"), ProductValue("9")),
            ProductVariant(ProductValue("75"), ProductValue("4"), ProductValue("12"))
        )
    }

    private fun getSubscriptionValue(): List<ProductVariant> {
        return listOf(
            ProductVariant(null, null, ProductValue("1")),
            ProductVariant(null, null, ProductValue("3")),
            ProductVariant(null, null, ProductValue("6")),
            ProductVariant(null, null, ProductValue("9")),
            ProductVariant(null, null, ProductValue("12"))
        )
    }
}