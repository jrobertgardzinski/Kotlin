package pl.jrobertgardzinski.kotlin.VO

import org.bson.types.Decimal128
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionVO(
        var transaction: TransactionDetails,
        var accountType: String,
        var customer: CustomerDetails
)

data class TransactionDetails(
        var date: LocalDateTime,
        var amount: Decimal128,
        var id: Int
)

data class CustomerDetails(
        var firstName: String,
        var lastName: String
)