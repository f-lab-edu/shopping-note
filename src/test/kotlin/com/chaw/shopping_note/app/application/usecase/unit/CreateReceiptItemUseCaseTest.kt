package com.chaw.shopping_note.app.application.usecase.unit

import com.chaw.shopping_note.app.receipt.application.dto.CreateReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.application.usecase.CreateReceiptItemUseCase
import com.chaw.shopping_note.app.receipt.domain.Category
import com.chaw.shopping_note.app.receipt.domain.Receipt
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.security.access.AccessDeniedException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import kotlin.test.assertFailsWith

class CreateReceiptItemUseCaseTest {

    private val receiptRepository = mockk<ReceiptRepository>(relaxed = true)
    private val receiptItemRepository = mockk<ReceiptItemRepository>(relaxed = true)

    private val createReceiptItemUseCase = CreateReceiptItemUseCase(
        receiptRepository,
        receiptItemRepository
    )

    @Test
    fun `영수증 항목을 생성할 수 있다`() = runTest {
        // given
        val receiptId = 1L
        val userId = 123L

        val receipt = Receipt(
            id = receiptId,
            userId = userId,
            storeId = 10L,
            purchaseAt = LocalDateTime.now(),
            totalPrice = 0.0,
            totalCount = 0,
            createdAt = LocalDateTime.now()
        )

        val input = CreateReceiptItemRequestDto(
            userId = userId,
            receiptId = receiptId,
            productName = "상품1",
            productCode = "P001",
            unitPrice = 5000.0,
            quantity = 2,
            category = Category.FOOD
        )

        coEvery { receiptRepository.findById(receiptId) } returns Mono.just(receipt)
        coEvery { receiptItemRepository.save(any()) } answers { Mono.just(firstArg<ReceiptItem>()) }
        coEvery { receiptItemRepository.findAllByReceiptId(receiptId) } returns Flux.empty()
        coEvery { receiptRepository.save(any()) } answers { Mono.just(firstArg()) }

        // when
        val result = createReceiptItemUseCase.execute(input)

        // then
        assertEquals(input.productName, result.productName)
        assertEquals(input.unitPrice * input.quantity, result.totalPrice)

        coVerify(exactly = 1) { receiptItemRepository.save(any()) }
        coVerify(exactly = 1) { receiptRepository.save(any()) }
    }

    @Test
    fun `userId가 다르면 AccessDeniedException이 발생한다`() = runTest {
        // given
        val receiptId = 1L
        val userId = 123L
        val wrongUserId = 999L

        val receipt = Receipt(
            id = receiptId,
            userId = userId,
            storeId = 10L,
            purchaseAt = LocalDateTime.now(),
            totalPrice = 0.0,
            totalCount = 0,
            createdAt = LocalDateTime.now()
        )

        val input = CreateReceiptItemRequestDto(
            userId = wrongUserId,
            receiptId = receiptId,
            productName = "상품1",
            productCode = "P001",
            unitPrice = 5000.0,
            quantity = 2,
            category = Category.FOOD
        )

        coEvery { receiptRepository.findById(receiptId) } returns Mono.just(receipt)

        // when & then
        assertFailsWith<AccessDeniedException> {
            createReceiptItemUseCase.execute(input)
        }
    }

    @Test
    fun `receiptId가 잘못되면 IllegalArgumentException이 발생한다`() = runTest {
        // given
        val wrongReceiptId = 999L

        val input = CreateReceiptItemRequestDto(
            userId = 123L,
            receiptId = wrongReceiptId,
            productName = "상품1",
            productCode = "P001",
            unitPrice = 5000.0,
            quantity = 2,
            category = Category.FOOD
        )

        coEvery { receiptRepository.findById(wrongReceiptId) } returns Mono.empty()

        // when & then
        assertFailsWith<IllegalArgumentException> {
            createReceiptItemUseCase.execute(input)
        }
    }
}
