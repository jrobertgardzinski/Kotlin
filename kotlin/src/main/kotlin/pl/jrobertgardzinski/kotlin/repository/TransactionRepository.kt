package pl.jrobertgardzinski.kotlin.repository

import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import pl.jrobertgardzinski.kotlin.model.AccountType
import pl.jrobertgardzinski.kotlin.model.Customer
import pl.jrobertgardzinski.kotlin.model.Transaction

@ExperimentalStdlibApi
interface TransactionRepository : MongoRepository<Transaction, Int> {

    fun findAllByOrderByTransactionAmount(): List<Transaction>

    @Query(value = "{ \$and: [ { 'accountType.\$id' : { \$in:?0 } }, { 'customerId.\$id' : { \$in:?1 } } ] }", sort = "{ transactionAmount: 1 }")
    fun findByAccountTypeInAndCustomerIdInOrderByTransactionAmount(accountTypes: List<Int>, customerIds: List<Int>): List<Transaction>

    @Query(value = "{ 'accountType.\$id' : { \$in:?0 } }", sort = "{ transactionAmount: 1 }")
    fun findByAccountTypeInOrderByTransactionAmount(accountTypes: List<Int>): List<Transaction>

    @Query(value = "{ 'customerId.\$id' : { \$in:?0 } }", sort = "{ transactionAmount: 1 }")
    fun findByCustomerIdInOrderByTransactionAmount(customerIds: List<Int>): List<Transaction>
}