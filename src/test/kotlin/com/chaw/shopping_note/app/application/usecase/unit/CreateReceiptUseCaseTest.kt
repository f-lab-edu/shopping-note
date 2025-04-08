package com.chaw.shopping_note.app.application.usecase.unit

import com.chaw.shopping_note.app.receipt.application.dto.CreateReceiptRequestDto
import com.chaw.shopping_note.app.receipt.application.usecase.CreateReceiptUseCase
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CreateReceiptUseCaseTest {

    private val receiptRepository = mockk<ReceiptRepository>()

    private val createReceiptUseCase = CreateReceiptUseCase(
        receiptRepository
    )

    @BeforeEach
    fun setUp() {
        every { receiptRepository.save(any()) } answers { firstArg() }
    }

    @Test
    fun `영수증을 생성할 수 있다`() {
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
        assertEquals(0.0, result.totalPrice)
        assertEquals(0, result.totalCount)
        assertEquals(input.purchaseAt, result.purchaseAt)

        verify(exactly = 1) { receiptRepository.save(any()) }
    }
}
