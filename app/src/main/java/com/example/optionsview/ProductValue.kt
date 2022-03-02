package com.example.optionsview

import androidx.core.text.isDigitsOnly

data class ProductValue(
    val value: String? = null
) {
    val valueInDouble by lazy {
        if (!value.isNullOrEmpty() && isDigit(value)) {
            value.toDouble()
        } else {
            0.0
        }
    }

    private fun isDigit(str: String?) = str?.isDigitsOnly()?.let { true } ?: false
}