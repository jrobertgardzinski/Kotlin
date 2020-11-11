package pl.jrobertgardzinski.kotlin.service

import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import pl.jrobertgardzinski.kotlin.VO.CustomerDetails
import pl.jrobertgardzinski.kotlin.VO.TransactionDetails
import pl.jrobertgardzinski.kotlin.VO.TransactionVO
import pl.jrobertgardzinski.kotlin.model.AccountType
import pl.jrobertgardzinski.kotlin.model.Customer
import pl.jrobertgardzinski.kotlin.model.Transaction
import pl.jrobertgardzinski.kotlin.repository.AccountTypeRepository
import pl.jrobertgardzinski.kotlin.repository.CustomerRepository
import pl.jrobertgardzinski.kotlin.repository.TransactionRepository
import kotlin.reflect.full.memberProperties

@Service
@ExperimentalStdlibApi
class TransactionsService constructor(
        private val transactionRepository: TransactionRepository
) {

    fun findByAccountTypesAndCustomerIds(accountTypes: List<String>, customerIds: List<String>) : List<TransactionVO> {
        val forAllAccountTypes : Boolean = accountTypes.get(0).equals("ALL")
        val forAllCustomerIds : Boolean = customerIds.get(0).equals("ALL")

        var result =
            if (forAllAccountTypes && forAllCustomerIds) {
                transactionRepository.findAllByOrderByTransactionAmount()
            }
            else if (forAllAccountTypes) {
                transactionRepository.findByCustomerIdInOrderByTransactionAmount(customerIds.map{ it.toInt() })
            }
            else if (forAllCustomerIds) {
                transactionRepository.findByAccountTypeInOrderByTransactionAmount(accountTypes.map{ it.toInt() })
            }
            else {
                transactionRepository.findByAccountTypeInAndCustomerIdInOrderByTransactionAmount(accountTypes.map{ it.toInt() }, customerIds.map{ it.toInt() })
            }

        return result.map { it.toTransactionVO() }
    }

    fun Transaction.toTransactionVO() = TransactionVO(
            transaction = TransactionDetails(
                    date = transactionDate,
                    amount = transactionAmount,
                    id = transactionId),
            accountType = accountType.name,
            customer = CustomerDetails(
                    firstName = customerId.firstName,
                    lastName = customerId.lastName)
    )
}