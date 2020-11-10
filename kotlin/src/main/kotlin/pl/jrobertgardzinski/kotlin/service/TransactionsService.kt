package pl.jrobertgardzinski.kotlin.service

import org.springframework.stereotype.Service
import pl.jrobertgardzinski.kotlin.VO.CustomerDetails
import pl.jrobertgardzinski.kotlin.VO.TransactionDetails
import pl.jrobertgardzinski.kotlin.VO.TransactionVO
import pl.jrobertgardzinski.kotlin.csv.model.AccountType
import pl.jrobertgardzinski.kotlin.csv.model.Customer
import pl.jrobertgardzinski.kotlin.csv.model.Transaction
import pl.jrobertgardzinski.kotlin.repository.AccountTypeRepository
import pl.jrobertgardzinski.kotlin.repository.CustomerRepository
import pl.jrobertgardzinski.kotlin.repository.TransactionRepository
import kotlin.reflect.full.memberProperties

@Service
@ExperimentalStdlibApi
class TransactionsService constructor(
        private val transactionRepository: TransactionRepository,
        private val accountTypeRepository: AccountTypeRepository,
        private val customerRepository: CustomerRepository
) {

    fun findByAccountTypesAndCustomerIds(accountTypes: List<String>, customerIds: List<String>) : List<TransactionVO> {
        val accountTypeEntities: List<AccountType> =
            if (accountTypes.get(0).equals("ALL")) {
                accountTypeRepository.findAll()
            }
            else {
                val ids = accountTypes.map { it.toInt() }
                accountTypeRepository.findAllById(ids).toList()
            }

        val customerEntities: List<Customer> =
                if (customerIds.get(0).equals("ALL")) {
                    customerRepository.findAll()
                }
                else {
                    val ids = customerIds.map { it.toInt() }
                    customerRepository.findAllById(ids).toList()
                }

        var result: List<Transaction> = transactionRepository.findByAccountTypeInAndCustomerIdInOrderByTransactionAmount(accountTypeEntities, customerEntities)
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