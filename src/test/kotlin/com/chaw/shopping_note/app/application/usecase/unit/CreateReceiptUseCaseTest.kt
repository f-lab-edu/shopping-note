package com.chaw.shopping_note.app.application.usecase.unit

import com.chaw.shopping_note.app.receipt.application.dto.CreateReceiptRequestDto
import com.chaw.shopping_note.app.receipt.application.usecase.CreateReceiptUseCase
import com.chaw.shopping_note.app.receipt.domain.Receipt
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import java.time.LocalDateTime

class CreateReceiptUseCaseTest {

    private val receiptRepository = mockk<ReceiptRepository>()

    private val createReceiptUseCase = CreateReceiptUseCase(
        receiptRepository
    )

    @Test
    fun success() = runTest {
        // given
        val input = createTestInput()
        mockRepositorySuccess()

        // when
        val result = createReceiptUseCase.execute(input)

        // then
        assertEquals(input.userId, result.userId)
        assertEquals(input.storeId, result.storeId)
        assertEquals(input.purchaseAt, result.purchaseAt)

        coVerify(exactly = 1) { receiptRepository.save(any()) }
    }

    private fun createTestInput(): CreateReceiptRequestDto {
        return CreateReceiptRequestDto(
            userId = 123L,
            storeId = 10L,
            purchaseAt = LocalDateTime.now()
        )
    }

    private fun mockRepositorySuccess() {
        coEvery { receiptRepository.save(any()) } answers { Mono.just(firstArg<Receipt>()) }
    }
}
