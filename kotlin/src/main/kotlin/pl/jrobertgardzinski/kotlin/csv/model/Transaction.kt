package pl.jrobertgardzinski.kotlin.csv.model

data class Transaction (
        val transaction_id: Int,
        val transaction_amount: String,
        val account_type: Int,
        val customer_id: Int,
        val transaction_date: String
)