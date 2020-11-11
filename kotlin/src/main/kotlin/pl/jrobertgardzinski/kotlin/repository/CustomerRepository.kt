package pl.jrobertgardzinski.kotlin.repository

import org.springframework.data.mongodb.repository.MongoRepository
import pl.jrobertgardzinski.kotlin.model.Customer

interface CustomerRepository : MongoRepository<Customer, Int> {
}