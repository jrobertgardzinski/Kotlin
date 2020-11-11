package pl.jrobertgardzinski.kotlin.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.jrobertgardzinski.kotlin.service.TransactionsService

@RestController
@ExperimentalStdlibApi
class TransactionsController constructor(
        private val transactionsService: TransactionsService
) {

    @GetMapping("/transactions")
    fun read(
            @RequestParam(defaultValue = "ALL") account_type: List<String>,
            @RequestParam(defaultValue = "ALL") customer_id: List<String>
    ) = transactionsService.findByAccountTypesAndCustomerIds(account_type, customer_id)

}