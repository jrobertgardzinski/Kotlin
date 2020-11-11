package pl.jrobertgardzinski.kotlin.repository

import org.springframework.data.mongodb.repository.MongoRepository
import pl.jrobertgardzinski.kotlin.model.AccountType

@ExperimentalStdlibApi
interface AccountTypeRepository : MongoRepository<AccountType, Int> {
}