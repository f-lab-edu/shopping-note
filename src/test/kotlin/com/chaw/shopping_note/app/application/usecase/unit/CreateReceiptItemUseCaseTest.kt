package com.chaw.shopping_note.app.application.usecase.unit

import com.chaw.shopping_note.app.receipt.application.dto.CreateReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.application.usecase.CreateReceiptItemUseCase
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class CreateReceiptItemUseCaseTest {

    private val receiptItemRepository = mockk<ReceiptItemRepository>(relaxed = true)

    private val createReceiptItemUseCase = CreateReceiptItemUseCase(
        receiptItemRepository,
    )

    @Test
    fun `영수증 항목을 생성할 수 있다`() = runTest {
        // given
        val receiptId = 1L
        val userId = 123L
        val categoryId = 1L

        val input = CreateReceiptItemRequestDto(
            userId = userId,
            categoryId = categoryId,
            receiptId = receiptId,
            productName = "상품1",
            productCode = "P001",
            unitPrice = 5000,
            quantity = 2,
        )

        coEvery { receiptItemRepository.save(any()) } answers { Mono.just(firstArg<ReceiptItem>()) }
        coEvery { receiptItemRepository.findAllByReceiptId(receiptId) } returns Flux.empty()

        // when
        val result = createReceiptItemUseCase.execute(input)

        // then
        assertEquals(input.productName, result.productName)
        assertEquals(input.unitPrice * input.quantity, result.totalPrice)

        coVerify(exactly = 1) { receiptItemRepository.save(any()) }
    }
}
