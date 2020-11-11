package pl.jrobertgardzinski.kotlin.controller

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

class TransactionRequestBuilder(private val mockMvc: MockMvc) {

    fun find(accountTypes: List<String>, customerIds: List<String>): ResultActions {
        return mockMvc.perform(
                get("/transactions")
                    .param("account_type", accountTypes.joinToString(","))
                    .param("customer_id", customerIds.joinToString(",")))
    }
}