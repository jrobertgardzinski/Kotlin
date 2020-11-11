package pl.jrobertgardzinski.kotlin.controller

import io.mockk.*
import io.mockk.impl.annotations.SpyK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import pl.jrobertgardzinski.kotlin.model.Transaction
import pl.jrobertgardzinski.kotlin.repository.TransactionRepository
import pl.jrobertgardzinski.kotlin.service.TransactionsService

@ExperimentalStdlibApi
class TransactionsControllerTest {

    @SpyK
    private lateinit var repository: TransactionRepository
    private lateinit var requestBuilder: TransactionRequestBuilder

    @BeforeEach
    fun configureSystemUnderTest() {
        repository = mockk()
        val service = TransactionsService(repository)

        val mockMvc = MockMvcBuilders
                .standaloneSetup(TransactionsController(service))
                    .build()
        requestBuilder = TransactionRequestBuilder(mockMvc)
    }

    @Nested
    inner class Find {

        private val ACCOUNT_TYPES = listOf<String>("1", "2")
        private val CUSTOMER_IDS = listOf<String>("1", "2")
        private val ACCOUNT_TYPES_as_LIST_OF_INTS = ACCOUNT_TYPES.map { it.toInt() }
        private val CUSTOMER_IDS_as_LIST_OF_INTS = ACCOUNT_TYPES.map { it.toInt() }

        @BeforeEach
        fun configureTestCases() {
            repositoryReturnsListOfTransactions()
        }

        private fun repositoryReturnsListOfTransactions() {
            every { repository.findByAccountTypeInAndCustomerIdInOrderByTransactionAmount(ACCOUNT_TYPES_as_LIST_OF_INTS, CUSTOMER_IDS_as_LIST_OF_INTS) } returns listOf<Transaction>()
            every { repository.findByAccountTypeInOrderByTransactionAmount(ACCOUNT_TYPES_as_LIST_OF_INTS) } returns listOf<Transaction>()
            every { repository.findByCustomerIdInOrderByTransactionAmount(CUSTOMER_IDS_as_LIST_OF_INTS) } returns listOf<Transaction>()
            every { repository.findAllByOrderByTransactionAmount() } returns listOf<Transaction>()
        }

        @Test
        @DisplayName("Should implicitly search for all accountTypes and customerIds")
        fun shouldImplicitlySearchForAllParameters() {
            requestBuilder.find(emptyList(), emptyList())
                    .andExpect(MockMvcResultMatchers.status().isOk)
        }

        @Test
        @DisplayName("Should call findAllByOrderByTransactionAmount()")
        fun shouldCallFindAllByOrderByTransactionAmount() {
            requestBuilder.find(emptyList(), emptyList())

            verify { repository.findAllByOrderByTransactionAmount() }
            confirmVerified(repository)
        }

        @Test
        @DisplayName("Should search for specified accountTypes and implicitly for all customerIds")
        fun shouldSearchForSpecifiedAccountTypesAndAllCustomerIds() {
            requestBuilder.find(ACCOUNT_TYPES, emptyList())
                    .andExpect(MockMvcResultMatchers.status().isOk)
        }

        @Test
        @DisplayName("Should call findByAccountTypeInOrderByTransactionAmount()")
        fun shouldCallFindByAccountTypeInOrderByTransactionAmount() {
            requestBuilder.find(ACCOUNT_TYPES, emptyList())

            verify { repository.findByAccountTypeInOrderByTransactionAmount(ACCOUNT_TYPES_as_LIST_OF_INTS) }
            confirmVerified(repository)
        }

        @Test
        @DisplayName("Should search for specified customerIds and implicitly for all accountTypes")
        fun shouldSearchForSpecifiedCustomerIdsAndAllAccountTypes() {
            requestBuilder.find(emptyList(), CUSTOMER_IDS)
                    .andExpect(MockMvcResultMatchers.status().isOk)
        }

        @Test
        @DisplayName("Should call findByAccountTypeInOrderByTransactionAmount()")
        fun shouldCallFindByCustomerIdInOrderByTransactionAmount() {
            requestBuilder.find(emptyList(), CUSTOMER_IDS)

            verify { repository.findByCustomerIdInOrderByTransactionAmount(CUSTOMER_IDS_as_LIST_OF_INTS) }
            confirmVerified(repository)
        }

        @Test
        @DisplayName("Should search for specified parameters")
        fun shouldSearchForSpecifiedParameters() {
            requestBuilder.find(ACCOUNT_TYPES, CUSTOMER_IDS)
                    .andExpect(MockMvcResultMatchers.status().isOk)
        }

        @Test
        @DisplayName("Should call findByAccountTypeInAndCustomerIdInOrderByTransactionAmount()")
        fun shouldCallFindByAccountTypeInAndCustomerIdInOrderByTransactionAmount() {
            requestBuilder.find(ACCOUNT_TYPES, CUSTOMER_IDS)

            verify { repository.findByAccountTypeInAndCustomerIdInOrderByTransactionAmount(ACCOUNT_TYPES_as_LIST_OF_INTS, CUSTOMER_IDS_as_LIST_OF_INTS) }
            confirmVerified(repository)
        }

    }
}