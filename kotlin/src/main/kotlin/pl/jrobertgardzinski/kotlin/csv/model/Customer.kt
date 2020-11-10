package pl.jrobertgardzinski.kotlin.csv.model

import java.math.BigDecimal

data class Customer (
        val id: Int,
        val firstName: String,
        val lastName: String,
        val lastLoginBalance: BigDecimal
)