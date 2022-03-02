package com.example.optionsview

data class ProductValue(
    val value: String? = null
) {
    val valueInDouble = value?.toDouble()
}