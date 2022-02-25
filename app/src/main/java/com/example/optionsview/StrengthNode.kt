package com.example.optionsview

import java.util.concurrent.atomic.AtomicReference

class StrengthNode : VariantNode() {
    var pricePerUnit = AtomicReference<Double>()
    var strengthIndex: Int = 0
}