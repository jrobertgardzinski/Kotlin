package pl.jrobertgardzinski.kotlin.repository

import org.springframework.data.mongodb.repository.MongoRepository
import pl.jrobertgardzinski.kotlin.csv.model.AccountType
import pl.jrobertgardzinski.kotlin.csv.model.Customer
import pl.jrobertgardzinski.kotlin.csv.model.Transaction

@ExperimentalStdlibApi
interface TransactionRepository : MongoRepository<Transaction, Int> {
    fun findByAccountTypeInAndCustomerIdInOrderByTransactionAmount(accountTypes: List<AccountType>, customerIds: List<Customer>): List<Transaction>
}