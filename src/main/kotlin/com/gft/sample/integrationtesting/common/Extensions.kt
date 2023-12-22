package com.gft.sample.integrationtesting.common

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.toAmountFormat(): BigDecimal {
    return this.setScale(2, RoundingMode.HALF_UP)
}
