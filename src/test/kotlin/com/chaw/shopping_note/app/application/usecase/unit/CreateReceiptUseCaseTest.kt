package com.chaw.shopping_note.app.application.usecase.unit

import com.chaw.shopping_note.app.receipt.application.dto.CreateReceiptRequestDto
import com.chaw.shopping_note.app.receipt.application.usecase.CreateReceiptUseCase
import com.chaw.shopping_note.app.receipt.domain.Receipt
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import reactor.core.publisher.Mono

class CreateReceiptUseCaseTest {

    private val receiptRepository = mockk<ReceiptRepository>()

    private val createReceiptUseCase = CreateReceiptUseCase(
        receiptRepository
    )

    @BeforeEach
    fun setUp() {
        coEvery { receiptRepository.save(any()) } answers { Mono.just(firstArg<Receipt>()) }
    }

    @Test
    fun success() = runTest {
        // given
        val input = CreateReceiptRequestDto(
            userId = 123L,
            storeId = 10L,
            purchaseAt = LocalDateTime.now()
        )

        // when
        val result = createReceiptUseCase.execute(input)

        // then
        assertEquals(input.userId, result.userId)
        assertEquals(input.storeId, result.storeId)
        assertEquals(input.purchaseAt, result.purchaseAt)

        coVerify(exactly = 1) { receiptRepository.save(any()) }
    }
}
