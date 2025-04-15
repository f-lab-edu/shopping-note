package com.chaw.shopping_note.app.application.usecase.unit

import com.chaw.shopping_note.app.receipt.application.dto.CreateReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.application.usecase.CreateReceiptItemUseCase
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import io.mockk.coEvery
import io.mockk.coVerify
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
    fun success() = runTest {
        // given
        val input = createTestInput()
        mockRepositorySuccess(input)

        // when
        val result = createReceiptItemUseCase.execute(input)

        // then
        assertEquals(input.productName, result.productName)
        assertEquals(input.unitPrice * input.quantity, result.totalPrice)

        coVerify(exactly = 1) { receiptItemRepository.save(any()) }
    }

    private fun createTestInput(): CreateReceiptItemRequestDto {
        return CreateReceiptItemRequestDto(
            userId = 1L,
            categoryId = 123L,
            receiptId = 1L,
            productName = "상품1",
            productCode = "P001",
            unitPrice = 5000,
            quantity = 2,
        )
    }

    private fun mockRepositorySuccess(input: CreateReceiptItemRequestDto) {
        coEvery { receiptItemRepository.save(any()) } answers { Mono.just(firstArg<ReceiptItem>()) }
        coEvery { receiptItemRepository.findAllByReceiptId(input.receiptId) } returns Flux.empty()
    }
}
