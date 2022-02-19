package com.example.optionsview

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class StrengthNode : VariantNode() {
    var pricePerUnit = AtomicReference<Double>()
    var defaultValue = AtomicBoolean(false)
}