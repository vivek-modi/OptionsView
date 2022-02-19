package com.example.optionsview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ActivityViewModel : ViewModel() {

    var baseNode: VariantNode = VariantNode()
    var strengthSearchIndex: Int = 0
    var quantitySearchIndex: Int = 0
    var subsriptionSearchIndex: Int = 0
    private val defaultValueId = "12643423243324"

    init {
        viewModelScope.launch {
            createGraph()
            getNodeDefaultValuePosition()
        }
    }

    private fun createGraph() {
        val tempHashMap: MutableMap<String, VariantNode> = mutableMapOf()
        val sortedList = getSortedList()

        sortedList.forEach { productVariant ->
            productVariant.strength?.let { strength ->
                val tempHashMapNode = tempHashMap["strength_${strength.value}"]
                if (tempHashMapNode != null) {
                    if (tempHashMapNode is StrengthNode) {
                        if (productVariant.id == defaultValueId) {
                            tempHashMapNode.defaultValue.compareAndSet(false, true)
                        }
                    }
                    return@let
                }
                val tempNode = StrengthNode().apply {
                    value = strength
                    pricePerUnit = productVariant.pricePerUnit?.value
                    if (productVariant.id == defaultValueId) {
                        defaultValue.compareAndSet(false, true)
                    }
                }
                baseNode.children.add(tempNode)
                tempHashMap["strength_${strength.value}"] = tempNode
            }
            productVariant.quantity?.let { quantity ->
                val tempHashMapNode =
                    tempHashMap["strength_${productVariant.strength?.value}_quantity_${quantity.value}"]
                if (tempHashMapNode != null) {
                    if (tempHashMapNode is QuantityNode) {
                        if (productVariant.id == defaultValueId) {
                            tempHashMapNode.defaultValue.compareAndSet(false, true)
                        }
                    }
                    return@let
                }
                val tempNode = QuantityNode().apply {
                    value = quantity
                    if (productVariant.id == defaultValueId) {
                        defaultValue.compareAndSet(false, true)
                    }
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
                    if (productVariant.id == defaultValueId) {
                        defaultValue.compareAndSet(false, true)
                    }
                }
                val parent =
                    tempHashMap["strength_${productVariant.strength?.value}_quantity_${productVariant.quantity?.value}"]
                        ?: baseNode
                parent.children.add(tempNode)
            }
        }
        baseNode
    }

    private fun getNodeDefaultValuePosition() {
        baseNode.children.mapIndexed { strengthIndex, strengthVariantNode ->
            if ((strengthVariantNode as StrengthNode).defaultValue.get()) {
                strengthSearchIndex = strengthIndex
            }
            strengthVariantNode.children.mapIndexed { quantityIndex, quantityVariantNode ->
                if ((quantityVariantNode as QuantityNode).defaultValue.get()) {
                    quantitySearchIndex = quantityIndex
                }
                quantityVariantNode.children.mapIndexed { index, variantNode ->
                    if ((variantNode as SubscriptionNode).defaultValue.get()) {
                        subsriptionSearchIndex = index
                    }
                }
            }
        }
    }

    private fun getSortedList(): List<ProductVariant> {
        val getUnSortedDataList = getUnSortedDataList()
        return getUnSortedDataList.sortedWith(
            compareBy<ProductVariant> {   // or compareByDescending
                it.strength?.value?.toInt() ?: 0   // or java.lang.Integer.MAX_VALUE
            }.thenBy {                    // or thenByDescending
                it.quantity?.value?.toInt() ?: 0   // or java.lang.Integer.MAX_VALUE
            }
        )
    }

    private fun getUnSortedDataList(): List<ProductVariant> {
        return listOf(
            ProductVariant(
                "12",
                ProductValue("75"),
                ProductValue("14"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "122",
                ProductValue("75"),
                ProductValue("14"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "132",
                ProductValue("75"),
                ProductValue("14"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "112",
                ProductValue("75"),
                ProductValue("14"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1442",
                ProductValue("75"),
                ProductValue("14"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "222",
                ProductValue("25"),
                ProductValue("8"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "552",
                ProductValue("25"),
                ProductValue("8"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "553242",
                ProductValue("25"),
                ProductValue("8"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "5522",
                ProductValue("25"),
                ProductValue("8"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1233",
                ProductValue("25"),
                ProductValue("8"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1442",
                ProductValue("50"),
                ProductValue("14"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1266",
                ProductValue("50"),
                ProductValue("14"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "7712",
                ProductValue("50"),
                ProductValue("14"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "442342",
                ProductValue("50"),
                ProductValue("14"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "324232",
                ProductValue("50"),
                ProductValue("14"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "2142",
                ProductValue("75"),
                ProductValue("10"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "4352",
                ProductValue("75"),
                ProductValue("10"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "5122",
                ProductValue("75"),
                ProductValue("10"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "23122",
                ProductValue("75"),
                ProductValue("10"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "23232",
                ProductValue("75"),
                ProductValue("10"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1232",
                ProductValue("25"),
                ProductValue("2"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1232",
                ProductValue("25"),
                ProductValue("2"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "13232",
                ProductValue("25"),
                ProductValue("2"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "122332",
                ProductValue("25"),
                ProductValue("2"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "11232",
                ProductValue("25"),
                ProductValue("2"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "123234",
                ProductValue("25"),
                ProductValue("4"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "123232",
                ProductValue("25"),
                ProductValue("4"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "123232",
                ProductValue("25"),
                ProductValue("4"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "13422",
                ProductValue("25"),
                ProductValue("4"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "13242",
                ProductValue("25"),
                ProductValue("4"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "122323",
                ProductValue("25"),
                ProductValue("6"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "13242",
                ProductValue("25"),
                ProductValue("6"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1322",
                ProductValue("25"),
                ProductValue("6"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1232324",
                ProductValue("25"),
                ProductValue("6"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12342",
                ProductValue("25"),
                ProductValue("6"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1234324",
                ProductValue("25"),
                ProductValue("10"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "13243242",
                ProductValue("25"),
                ProductValue("10"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "13242",
                ProductValue("25"),
                ProductValue("10"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "13422",
                ProductValue("25"),
                ProductValue("10"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12342",
                ProductValue("25"),
                ProductValue("10"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1232423",
                ProductValue("25"),
                ProductValue("12"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "123244",
                ProductValue("25"),
                ProductValue("12"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "13422",
                ProductValue("25"),
                ProductValue("12"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12342",
                ProductValue("25"),
                ProductValue("12"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12324",
                ProductValue("25"),
                ProductValue("12"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12324342",
                ProductValue("25"),
                ProductValue("14"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "123243",
                ProductValue("25"),
                ProductValue("14"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "132432",
                ProductValue("25"),
                ProductValue("14"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "21312",
                ProductValue("25"),
                ProductValue("14"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1324342",
                ProductValue("25"),
                ProductValue("14"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12342",
                ProductValue("50"),
                ProductValue("2"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12342",
                ProductValue("50"),
                ProductValue("2"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12342",
                ProductValue("50"),
                ProductValue("2"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12324",
                ProductValue("50"),
                ProductValue("2"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12324",
                ProductValue("50"),
                ProductValue("2"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "13242",
                ProductValue("50"),
                ProductValue("4"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1232434",
                ProductValue("50"),
                ProductValue("4"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1223455",
                ProductValue("50"),
                ProductValue("4"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643",
                ProductValue("50"),
                ProductValue("4"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643324",
                ProductValue("50"),
                ProductValue("4"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643243",
                ProductValue("50"),
                ProductValue("6"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643243",
                ProductValue("50"),
                ProductValue("6"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643243",
                ProductValue("50"),
                ProductValue("6"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643243",
                ProductValue("50"),
                ProductValue("6"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643324324",
                ProductValue("50"),
                ProductValue("6"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643332423424",
                ProductValue("50"),
                ProductValue("12"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643243243324",
                ProductValue("50"),
                ProductValue("12"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643243",
                ProductValue("50"),
                ProductValue("12"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643423423",
                ProductValue("50"),
                ProductValue("12"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643423243324",
                ProductValue("50"),
                ProductValue("12"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643243",
                ProductValue("75"),
                ProductValue("2"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12642343",
                ProductValue("75"),
                ProductValue("2"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643098",
                ProductValue("75"),
                ProductValue("2"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "3254",
                ProductValue("75"),
                ProductValue("2"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12642342343",
                ProductValue("75"),
                ProductValue("2"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643242343",
                ProductValue("75"),
                ProductValue("6"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "32432",
                ProductValue("75"),
                ProductValue("6"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "126432433",
                ProductValue("75"),
                ProductValue("6"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "126433243",
                ProductValue("75"),
                ProductValue("6"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643423443",
                ProductValue("75"),
                ProductValue("6"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1264324343",
                ProductValue("75"),
                ProductValue("8"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "126433423432",
                ProductValue("75"),
                ProductValue("8"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1264332434234",
                ProductValue("75"),
                ProductValue("8"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643243",
                ProductValue("75"),
                ProductValue("8"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12641323",
                ProductValue("75"),
                ProductValue("8"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1264332",
                ProductValue("75"),
                ProductValue("12"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643132",
                ProductValue("75"),
                ProductValue("12"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1264323",
                ProductValue("75"),
                ProductValue("12"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1264332434324",
                ProductValue("75"),
                ProductValue("12"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12642342343324",
                ProductValue("75"),
                ProductValue("12"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1266576743324",
                ProductValue("50"),
                ProductValue("8"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1256765643324",
                ProductValue("50"),
                ProductValue("8"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "16752643324",
                ProductValue("50"),
                ProductValue("8"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12756643324",
                ProductValue("50"),
                ProductValue("8"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643321",
                ProductValue("50"),
                ProductValue("8"),
                ProductValue("12"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643234",
                ProductValue("75"),
                ProductValue("4"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "126437657657324",
                ProductValue("75"),
                ProductValue("4"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12645765673324",
                ProductValue("75"),
                ProductValue("4"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643324324",
                ProductValue("75"),
                ProductValue("4"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1264324323",
                ProductValue("75"),
                ProductValue("4"),
                ProductValue("12"),
                ProductValue("1.50")
            )
        )
    }

    private fun getSubscriptionValue(): List<ProductVariant> {
        return listOf(
            ProductVariant("1232643", null, null, ProductValue("1")),
            ProductVariant("1324322643", null, null, ProductValue("3")),
            ProductVariant("32432432", null, null, ProductValue("6")),
            ProductVariant("324234324", null, null, ProductValue("9")),
            ProductVariant("32432432434", null, null, ProductValue("12"))
        )
    }
}