package pl.jrobertgardzinski.kotlin.repository

import org.springframework.data.mongodb.repository.MongoRepository
import pl.jrobertgardzinski.kotlin.model.AccountType
import pl.jrobertgardzinski.kotlin.model.Customer
import pl.jrobertgardzinski.kotlin.model.Transaction

@ExperimentalStdlibApi
interface TransactionRepository : MongoRepository<Transaction, Int> {
    fun findByAccountTypeInAndCustomerIdInOrderByTransactionAmount(accountTypes: List<AccountType>, customerIds: List<Customer>): List<Transaction>
}