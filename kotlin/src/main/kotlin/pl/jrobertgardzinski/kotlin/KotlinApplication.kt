package pl.jrobertgardzinski.kotlin

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.bson.types.Decimal128
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PropertiesLoaderUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import pl.jrobertgardzinski.kotlin.component.MongoInitializer
import pl.jrobertgardzinski.kotlin.model.AccountType
import pl.jrobertgardzinski.kotlin.model.Customer
import pl.jrobertgardzinski.kotlin.model.Transaction
import pl.jrobertgardzinski.kotlin.repository.AccountTypeRepository
import pl.jrobertgardzinski.kotlin.repository.CustomerRepository
import pl.jrobertgardzinski.kotlin.repository.TransactionRepository
import java.io.BufferedReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@ExperimentalStdlibApi
@SpringBootApplication
class KotlinApplication(
		private var mongoInitializer: MongoInitializer
) {
	init {
		mongoInitializer.reloadData()
	}
}

@ExperimentalStdlibApi
fun main(args: Array<String>) {
	runApplication<KotlinApplication>(*args) {
		addInitializers(beans {
			bean {
				fun propertyToUser ( property: Map.Entry<Any, Any> ) = User.withDefaultPasswordEncoder().username(property.key as String).password(property.value  as String).roles("USER").build()

				val propertiesFile: Resource = ClassPathResource("users.properties")
				val properties: Properties = PropertiesLoaderUtils.loadProperties(propertiesFile)
				var users = mutableListOf<UserDetails>()
				properties.forEach {
					users.add( propertyToUser(it) )
				}
				InMemoryUserDetailsManager(users)
			}
		})
	}
}

fun getResourceAsInputStream(path: String): BufferedReader {
	return object {}.javaClass.getResource(path).openStream().bufferedReader()
}