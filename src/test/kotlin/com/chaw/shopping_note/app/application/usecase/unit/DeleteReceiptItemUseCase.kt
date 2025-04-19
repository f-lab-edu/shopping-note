package com.chaw.shopping_note.app.application.usecase.unit

import com.chaw.shopping_note.app.receipt.application.dto.DeleteReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.application.usecase.DeleteReceiptItemUseCase
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.access.AccessDeniedException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import kotlin.test.assertFailsWith

class DeleteReceiptItemUseCaseTest {

    private val receiptRepository = mockk<ReceiptRepository>()
    private val receiptItemRepository = mockk<ReceiptItemRepository>()

    private val deleteReceiptItemUseCase = DeleteReceiptItemUseCase(
        receiptRepository,
        receiptItemRepository
    )

    @BeforeEach
    fun setUp() {
        coEvery { receiptRepository.save(any()) } answers { Mono.just(firstArg()) }
        coEvery { receiptItemRepository.delete(any()) } answers { Mono.empty() }
    }

    @Test
    fun `영수증 항목을 삭제할 수 있다`() = runTest {
        // given
        val receiptId = 1L
        val receiptItemId = 100L
        val userId = 123L

        val receipt = Receipt(
            id = receiptId,
            userId = userId,
            storeId = 10L,
            purchaseAt = LocalDateTime.now(),
            totalPrice = 10000,
            totalCount = 2,
            createdAt = LocalDateTime.now()
        )

        val receiptItem = ReceiptItem(
            id = receiptItemId,
            receiptId = receiptId,
            productName = "상품1",
            productCode = "P001",
            unitPrice = 5000,
            quantity = 2,
            totalPrice = 10000,
            category = Category.FOOD,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        coEvery { receiptItemRepository.findById(receiptItemId) } returns Mono.just(receiptItem)
        coEvery { receiptRepository.findById(receiptId) } returns Mono.just(receipt)
        coEvery { receiptItemRepository.findAllByReceiptId(receiptId) } returns Flux.empty()

        val input = DeleteReceiptItemRequestDto(
            userId = userId,
            receiptItemId = receiptItemId
        )

        // when
        val result = deleteReceiptItemUseCase.execute(input)

        // then
        assertTrue(result)

        coVerify(exactly = 1) { receiptItemRepository.delete(receiptItem) }
        coVerify(exactly = 1) { receiptRepository.save(any()) }
    }

    @Test
    fun `userId가 다르면 AccessDeniedException이 발생한다`() = runTest {
        // given
        val receiptId = 1L
        val receiptItemId = 100L
        val userId = 123L
        val wrongUserId = 999L

        val receipt = Receipt(
            id = receiptId,
            userId = userId,
            storeId = 10L,
            purchaseAt = LocalDateTime.now(),
            totalPrice = 10000,
            totalCount = 2,
            createdAt = LocalDateTime.now()
        )

        val receiptItem = ReceiptItem(
            id = receiptItemId,
            receiptId = receiptId,
            productName = "상품1",
            productCode = "P001",
            unitPrice = 5000,
            quantity = 2,
            totalPrice = 10000,
            category = Category.FOOD,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        coEvery { receiptItemRepository.findById(receiptItemId) } returns Mono.just(receiptItem)
        coEvery { receiptRepository.findById(receiptId) } returns Mono.just(receipt)

        val input = DeleteReceiptItemRequestDto(
            userId = wrongUserId,
            receiptItemId = receiptItemId
        )

        // when & then
        assertFailsWith<AccessDeniedException> {
            deleteReceiptItemUseCase.execute(input)
        }
    }

    @Test
    fun `receiptItemId가 잘못되면 IllegalArgumentException이 발생한다`() = runTest {
        // given
        val wrongReceiptItemId = 999L

        coEvery { receiptItemRepository.findById(wrongReceiptItemId) } returns Mono.empty()

        val input = DeleteReceiptItemRequestDto(
            userId = 123L,
            receiptItemId = wrongReceiptItemId
        )

        // when & then
        assertFailsWith<IllegalArgumentException> {
            deleteReceiptItemUseCase.execute(input)
        }
    }
}
