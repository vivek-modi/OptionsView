package com.example.optionsview

import java.util.concurrent.atomic.AtomicBoolean

class StrengthNode : VariantNode() {
    var pricePerUnit: String? = null
    var defaultValue = AtomicBoolean(false)
}