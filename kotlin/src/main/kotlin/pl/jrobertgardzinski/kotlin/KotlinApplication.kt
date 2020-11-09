package pl.jrobertgardzinski.kotlin

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.vhl.blackmo.grass.dsl.grass
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import pl.jrobertgardzinski.kotlin.csv.model.AccountType
import pl.jrobertgardzinski.kotlin.csv.model.Customer
import pl.jrobertgardzinski.kotlin.csv.model.Transaction
import pl.jrobertgardzinski.kotlin.repository.AccountTypeRepository
import pl.jrobertgardzinski.kotlin.repository.CustomerRepository
import pl.jrobertgardzinski.kotlin.repository.TransactionRepository
import java.io.InputStream

@ExperimentalStdlibApi
@SpringBootApplication
class KotlinApplication(
		private val customerRepository: CustomerRepository,
		private val accountTypeRepository: AccountTypeRepository,
		private val transactionRepository: TransactionRepository
) {
	init {
		val csvAccountTypes = csvReader().readAllWithHeader(getResourceAsInputStream("/static/accountypes.csv"))
		val accountTypeEntities = grass<AccountType>().harvest(csvAccountTypes)
		accountTypeRepository.deleteAll()
		accountTypeRepository.saveAll(accountTypeEntities)

		val csvCustomers = csvReader().readAllWithHeader(getResourceAsInputStream("/static/customers.csv"))
		val customerEntities = grass<Customer>().harvest(csvCustomers)
		customerRepository.deleteAll()
		customerRepository.saveAll(customerEntities)

		val csvTransactions = csvReader().readAllWithHeader(getResourceAsInputStream("/static/transactions.csv"))
		val transactionEntities = grass<Transaction>().harvest(csvTransactions)
		transactionRepository.deleteAll()
		transactionRepository.saveAll(transactionEntities)
	}
}

@ExperimentalStdlibApi
fun main(args: Array<String>) {
	runApplication<KotlinApplication>(*args)
}

fun getResourceAsInputStream(path: String): InputStream {
	return object {}.javaClass.getResource(path).openStream()
}