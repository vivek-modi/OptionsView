package com.example.optionsview

data class ProductVariant(
    val strength: ProductValue? = null,
    val quantity: ProductValue? = null,
    val subscription: ProductValue? = null,
    val pricePerUnit: ProductValue? = null
)
