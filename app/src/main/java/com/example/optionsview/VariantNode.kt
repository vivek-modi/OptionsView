package com.example.optionsview

open class VariantNode {
    var value: ProductValue? = null
    var children: MutableList<VariantNode> = arrayListOf()
    var defaultValue: Boolean? = null
}