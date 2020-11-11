package pl.jrobertgardzinski.kotlin.model

import org.springframework.data.annotation.Id
import java.util.*

@ExperimentalStdlibApi
data class AccountType (
        @Id val accountType: Int,
        val name: String
)