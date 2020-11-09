package pl.jrobertgardzinski.kotlin.repository

import org.springframework.data.mongodb.repository.MongoRepository
import pl.jrobertgardzinski.kotlin.csv.model.Transaction

interface TransactionRepository : MongoRepository<Transaction, Int> {
}