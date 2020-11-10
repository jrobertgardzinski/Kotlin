package pl.jrobertgardzinski.kotlin.csv.model

import org.bson.types.Decimal128
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import java.math.BigDecimal
import java.time.LocalDateTime

@ExperimentalStdlibApi
data class Transaction constructor(
        @Id val transactionId: Int,
        val transactionAmount: Decimal128,
        @DBRef val accountType: AccountType,
        @DBRef val customerId: Customer,
        val transactionDate: LocalDateTime
)