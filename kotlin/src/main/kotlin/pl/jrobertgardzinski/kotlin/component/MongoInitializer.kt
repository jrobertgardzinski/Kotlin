package pl.jrobertgardzinski.kotlin.component

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.bson.types.Decimal128
import org.springframework.stereotype.Component
import pl.jrobertgardzinski.kotlin.getResourceAsInputStream
import pl.jrobertgardzinski.kotlin.model.AccountType
import pl.jrobertgardzinski.kotlin.model.Customer
import pl.jrobertgardzinski.kotlin.model.Transaction
import pl.jrobertgardzinski.kotlin.repository.AccountTypeRepository
import pl.jrobertgardzinski.kotlin.repository.CustomerRepository
import pl.jrobertgardzinski.kotlin.repository.TransactionRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@ExperimentalStdlibApi
@Component class MongoInitializer constructor(
        private val customerRepository: CustomerRepository,
        private val accountTypeRepository: AccountTypeRepository,
        private val transactionRepository: TransactionRepository
) {

    fun reloadData() {
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