package pl.jrobertgardzinski.kotlin.csv.model

import org.springframework.data.annotation.Id
import java.math.BigDecimal

data class Customer (
        @Id val id: Int,
        val firstName: String,
        val lastName: String,
        val lastLoginBalance: BigDecimal
)