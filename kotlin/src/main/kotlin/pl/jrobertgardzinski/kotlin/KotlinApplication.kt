package pl.jrobertgardzinski.kotlin

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import org.bson.types.Decimal128
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import pl.jrobertgardzinski.kotlin.csv.model.AccountType
import pl.jrobertgardzinski.kotlin.csv.model.Customer
import pl.jrobertgardzinski.kotlin.csv.model.Transaction
import pl.jrobertgardzinski.kotlin.repository.AccountTypeRepository
import pl.jrobertgardzinski.kotlin.repository.CustomerRepository
import pl.jrobertgardzinski.kotlin.repository.TransactionRepository
import java.io.BufferedReader
import java.io.InputStream
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@ExperimentalStdlibApi
@SpringBootApplication
class KotlinApplication(
		private val customerRepository: CustomerRepository,
		private val accountTypeRepository: AccountTypeRepository,
		private val transactionRepository: TransactionRepository
) {
	init {
		clearRepositories()

		loadAndSaveAccountTypes()
		loadAndSaveCustomers()
		loadAndSaveTransactions()
	}

	private fun clearRepositories() {
		accountTypeRepository.deleteAll()
		customerRepository.deleteAll()
		transactionRepository.deleteAll()
	}

	private fun loadAndSaveAccountTypes() {


		val accountTypeEntities = mutableListOf<AccountType>()
		val reader = getResourceAsInputStream("/static/accountypes.csv")
		val csvParser = CSVParser(reader, CSVFormat.DEFAULT
				.withHeader("account_type", "name")
				.withFirstRecordAsHeader()
				.withIgnoreHeaderCase()
				.withTrim())
		for (csvRecord in csvParser) {
			if (csvRecord.all { it.isEmpty() }) {
				continue
			}
			val accountType = csvRecord.get("account_type").toInt()
			val name = csvRecord.get("name")
			accountTypeEntities.add(AccountType(accountType, name))
		}
		accountTypeRepository.saveAll(accountTypeEntities)
	}

	private fun loadAndSaveCustomers() {
		val customerEntities = mutableListOf<Customer>()
		val reader = getResourceAsInputStream("/static/customers.csv")
		val csvParser = CSVParser(reader, CSVFormat.DEFAULT
				.withHeader("id", "first_name", "last_name", "last_login_balance")
				.withFirstRecordAsHeader()
				.withIgnoreHeaderCase()
				.withTrim())
		for (csvRecord in csvParser) {
			if (csvRecord.all { it.isEmpty() }) {
				continue
			}
			val id = csvRecord.get("id").toInt()
			val firstName = csvRecord.get("first_name")
			val lastName = csvRecord.get("last_name")
			val lastLoginBalance = csvRecord.get("last_login_balance").replace(",", ".").toBigDecimal()
			customerEntities.add(Customer(id, firstName, lastName, lastLoginBalance))
		}
		customerRepository.saveAll(customerEntities)
	}

	private fun loadAndSaveTransactions() {
		val transactionEntities = mutableListOf<Transaction>()
		val reader = getResourceAsInputStream("/static/transactions.csv")
		val csvParser = CSVParser(reader, CSVFormat.DEFAULT
				.withHeader("transaction_id", "transaction_amount", "account_type", "customer_id","transaction_date")
				.withFirstRecordAsHeader()
				.withIgnoreHeaderCase()
				.withTrim())
		for (csvRecord in csvParser) {
			if (csvRecord.all { it.isEmpty() }) {
				continue
			}
			val transactionId = csvRecord.get("transaction_id").toInt()
			val transactionAmount = Decimal128.parse(csvRecord.get("transaction_amount").replace(",", "."))
			val accountType = accountTypeRepository.findById(csvRecord.get("account_type").toInt()).get()
			val customerId = customerRepository.findById(csvRecord.get("customer_id").toInt()).get()
			val dateString = csvRecord.get("transaction_date")
			val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
			val transactionDate = LocalDateTime.parse(dateString, formatter)
			transactionEntities.add(Transaction(transactionId, transactionAmount, accountType, customerId, transactionDate))
		}
		transactionRepository.saveAll(transactionEntities)
	}

}

@ExperimentalStdlibApi
fun main(args: Array<String>) {
	runApplication<KotlinApplication>(*args)
}

fun getResourceAsInputStream(path: String): BufferedReader {
	return object {}.javaClass.getResource(path).openStream().bufferedReader()
}