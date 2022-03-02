package com.example.optionsview

import androidx.lifecycle.ViewModel
import kotlinx.atomicfu.atomic
import java.util.concurrent.atomic.AtomicReference

class ActivityViewModel : ViewModel() {

    var baseNode: VariantNode = VariantNode()
    var strengthDefaultIndex = 0
    var quantityDefaultIndex = 0
    var subscriptionDefaultIndex = 0
    var isFirstTime = atomic(true)

    //    private val defaultValueId = "1232"  // 25 2 1
//    private val defaultValueId = "10923232"  // 25 2 3
//    private val defaultValueId = "13232"  // 25 2 6
//    private val defaultValueId = "122332"  // 25 2 9
//    private val defaultValueId = "11232"  // 25 2 12
//    private val defaultValueId = "123234"  // 25 4 1
//    private val defaultValueId = "123232"  // 25 4 3
//    private val defaultValueId = "1232090932"  // 25 4 6
//    private val defaultValueId = "13422"  // 25 4 9
//    private val defaultValueId = "13209842"  // 25 4 12
//    private val defaultValueId = "122323"  // 25 6 1
//    private val defaultValueId = "13240880802"  // 25 6 3
//    private val defaultValueId = "1322"  // 25 6 6
//    private val defaultValueId = "1232324"  // 25 6 9
//    private val defaultValueId = "12342787283"  // 25 6 12
//    private val defaultValueId = "222"  // 25 8 1
//    private val defaultValueId = "12234234342"  // 50 2 1
//    private val defaultValueId = "123545642"  // 50 2 3
//    private val defaultValueId = "132489982"  // 50 4 1
//    private val defaultValueId = "12643232443"  // 50 6 1
//    private val defaultValueId = "1266576743324"  // 50 8 1
//    private val defaultValueId = "12643249782343"  // 75 2 1
    private val defaultValueId = "12643243"  // 75 8 9
//    private val defaultValueId = "130922"  // 100 2 1
//    private val defaultValueId = "32432432"  // 6

    init {
        createGraph()
    }

    private fun createGraph() {
        val tempHashMap: MutableMap<String, VariantNode> = mutableMapOf()
        val sortedList = getSortedList()

        sortedList.mapIndexed { _, productVariant ->
            productVariant.strength?.let { strength ->
                val tempHashMapNode = tempHashMap["strength_${strength.value}"]
                val childrenSize = baseNode.children.size
                if (tempHashMapNode != null) {
                    if (tempHashMapNode is StrengthNode) {
                        if (productVariant.id == defaultValueId) {
                            tempHashMapNode.isPopular = true
                            strengthDefaultIndex = if (childrenSize == 0) {
                                childrenSize
                            } else {
                                childrenSize.minus(1)
                            }
                        }
                        productVariant.pricePerUnit?.valueInDouble
                            ?.let {
                                tempHashMapNode.pricePerUnit.compareAndSetIfLess(it)
                            }
                    }
                    return@let
                }
                val tempNode = StrengthNode().apply {
                    value = strength
                    pricePerUnit.set(productVariant.pricePerUnit?.valueInDouble)

                    if (productVariant.id == defaultValueId) {
                        isPopular = true
                        strengthDefaultIndex = childrenSize
                    }
                }
                baseNode.children.add(tempNode)
                tempHashMap["strength_${strength.value}"] = tempNode
            }
            productVariant.quantity?.let { quantity ->
                val strengthNode = baseNode.children.lastOrNull()
                if (strengthNode is StrengthNode) {
                    val strengthNodeChildrenSize = strengthNode.children.size
                    val tempHashMapNode =
                        tempHashMap["strength_${productVariant.strength?.value}_quantity_${quantity.value}"]
                    if (tempHashMapNode != null) {
                        if (productVariant.id == defaultValueId) {
                            quantityDefaultIndex = if (strengthNodeChildrenSize == 0) {
                                strengthNodeChildrenSize
                            } else {
                                strengthNodeChildrenSize.minus(1)
                            }
                        }
                        return@let
                    }
                    val tempNode = VariantNode().apply {
                        value = quantity
                        if (productVariant.id == defaultValueId) {
                            quantityDefaultIndex = strengthNodeChildrenSize
                        }
                    }
                    val parent =
                        tempHashMap["strength_${productVariant.strength?.value}"] ?: baseNode
                    parent.children.add(tempNode)
                    tempHashMap["strength_${productVariant.strength?.value}_quantity_${quantity.value}"] =
                        tempNode
                }
            }
            productVariant.subscription?.let { subscription ->
                val tempNode = SubscriptionNode().apply {
                    value = subscription
                    this.productVariant = productVariant
                }
                val parent =
                    tempHashMap["strength_${productVariant.strength?.value}_quantity_${productVariant.quantity?.value}"]
                        ?: baseNode
                if (productVariant.id == defaultValueId) {
                    subscriptionDefaultIndex = parent.children.size
                }
                parent.priorityQueue.add(tempNode)
                parent.children.add(tempNode)
            }
        }
        baseNode
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

    private fun AtomicReference<Double>.compareAndSetIfLess(newValue: Double): Boolean {
        do {
            val oldValue = get()
            if (newValue > oldValue) {
                return false
            }
        } while (!compareAndSet(oldValue, newValue))
        return true
    }

    private fun getUnSortedDataList(): List<ProductVariant> {
        return listOf(
            ProductVariant(
                "3412012",
                ProductValue("200"),
                ProductValue("2"),
                ProductValue("1"),
                ProductValue("54.50")
            ),
            ProductVariant(
                "3412012",
                ProductValue("175"),
                ProductValue("2"),
                ProductValue("1"),
                ProductValue("12.50")
            ),
            ProductVariant(
                "34122323012",
                ProductValue("150"),
                ProductValue("2"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "3412012",
                ProductValue("125"),
                ProductValue("2"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "130922",
                ProductValue("100"),
                ProductValue("2"),
                ProductValue("1"),
                ProductValue("5.50")
            ),
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
                ProductValue("4.83")
            ),
            ProductVariant(
                "10923232",
                ProductValue("25"),
                ProductValue("2"),
                ProductValue("3"),
                ProductValue("4.39")
            ),
            ProductVariant(
                "13232",
                ProductValue("25"),
                ProductValue("2"),
                ProductValue("6"),
                ProductValue("3.58")
            ),
            ProductVariant(
                "122332",
                ProductValue("25"),
                ProductValue("2"),
                ProductValue("9"),
                ProductValue("3.50")
            ),
            ProductVariant(
                "11232",
                ProductValue("25"),
                ProductValue("2"),
                ProductValue("12"),
                ProductValue("3.46")
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
                "1232090932",
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
                "13209842",
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
                "13240880802",
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
                "12342787283",
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
                "13208808093342",
                ProductValue("25"),
                ProductValue("10"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "13429086372",
                ProductValue("25"),
                ProductValue("10"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1237346646442",
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
                "13098789422",
                ProductValue("25"),
                ProductValue("12"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12309756742",
                ProductValue("25"),
                ProductValue("12"),
                ProductValue("9"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1085382324",
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
                ProductValue("0.50")
            ),
            ProductVariant(
                "12234234342",
                ProductValue("50"),
                ProductValue("2"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "123545642",
                ProductValue("50"),
                ProductValue("2"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "467612342",
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
                "132489982",
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
                "12643232443",
                ProductValue("50"),
                ProductValue("6"),
                ProductValue("1"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643324243",
                ProductValue("50"),
                ProductValue("6"),
                ProductValue("3"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12642343243",
                ProductValue("50"),
                ProductValue("6"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "12643098243",
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
                "1264324983",
                ProductValue("50"),
                ProductValue("12"),
                ProductValue("6"),
                ProductValue("1.50")
            ),
            ProductVariant(
                "1264342389423",
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
                "12643249782343",
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
            ProductVariant("1232643", null, null, ProductValue("1"), ProductValue("4.83")),
            ProductVariant("1324322643", null, null, ProductValue("3"), ProductValue("4.39")),
            ProductVariant("32432432", null, null, ProductValue("6"), ProductValue("3.58")),
            ProductVariant("324234324", null, null, ProductValue("9"), ProductValue("3.50")),
            ProductVariant("32432432434", null, null, ProductValue("12"), ProductValue("3.46"))
        )
    }
}