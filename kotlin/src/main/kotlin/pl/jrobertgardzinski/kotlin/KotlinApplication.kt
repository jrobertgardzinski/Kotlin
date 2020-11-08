package pl.jrobertgardzinski.kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class KotlinApplication

fun main(args: Array<String>) {
	runApplication<KotlinApplication>(*args)
}
