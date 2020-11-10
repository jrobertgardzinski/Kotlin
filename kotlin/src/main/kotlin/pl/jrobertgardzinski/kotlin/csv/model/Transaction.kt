package pl.jrobertgardzinski.kotlin.csv.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction (
        val transactionId: Int,
        val transactionAmount: BigDecimal,
        val accountType: Int,
        val customerId: Int,
        val transactionDate: LocalDateTime
)