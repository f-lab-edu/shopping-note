package com.chaw.shopping_note.app.application.usecase.unit

import com.chaw.shopping_note.app.receipt.application.dto.UpdateReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.application.usecase.UpdateReceiptItemUseCase
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

class UpdateReceiptItemUseCaseTest {

    private val receiptRepository = mockk<ReceiptRepository>()
    private val receiptItemRepository = mockk<ReceiptItemRepository>()

    private val updateReceiptItemUseCase = UpdateReceiptItemUseCase(
        receiptRepository,
        receiptItemRepository
    )

    @BeforeEach
    fun setUp() {
        coEvery { receiptRepository.save(any()) } answers { Mono.just(firstArg()) }
        coEvery { receiptItemRepository.save(any()) } answers { Mono.just(firstArg()) }
    }

    @Test
    fun `영수증 항목을 수정할 수 있다`() = runTest {
        // given
        val receiptId = 1L
        val receiptItemId = 100L
        val userId = 123L

        val receipt = Receipt(
            id = receiptId,
            userId = userId,
            storeId = 10L,
            purchaseAt = LocalDateTime.now(),
            totalPrice = 10000.0,
            totalCount = 2,
            createdAt = LocalDateTime.now()
        )

        val receiptItem = ReceiptItem(
            id = receiptItemId,
            receiptId = receiptId,
            productName = "상품1",
            productCode = "P001",
            unitPrice = 5000.0,
            quantity = 2,
            totalPrice = 10000.0,
            category = Category.FOOD,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        coEvery { receiptItemRepository.findById(receiptItemId) } returns Mono.just(receiptItem)
        coEvery { receiptRepository.findById(receiptId) } returns Mono.just(receipt)
        coEvery { receiptItemRepository.findAllByReceiptId(receiptId) } returns Flux.fromIterable(listOf(receiptItem))

        val input = UpdateReceiptItemRequestDto(
            userId = userId,
            receiptItemId = receiptItemId,
            productName = "수정된상품",
            productCode = "P999",
            unitPrice = 6000.0,
            quantity = 3,
            category = Category.HEALTH
        )

        // when
        val result = updateReceiptItemUseCase.execute(input)

        // then
        assertEquals("수정된상품", result.productName)
        assertEquals("P999", result.productCode)
        assertEquals(6000.0, result.unitPrice)
        assertEquals(3, result.quantity)
        assertEquals(6000.0 * 3, result.totalPrice)
        assertEquals(Category.HEALTH, result.category)

        coVerify(exactly = 1) { receiptItemRepository.save(receiptItem) }
        coVerify(exactly = 1) { receiptRepository.save(receipt) }
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
            totalPrice = 10000.0,
            totalCount = 2,
            createdAt = LocalDateTime.now()
        )

        val receiptItem = ReceiptItem(
            id = receiptItemId,
            receiptId = receiptId,
            productName = "상품1",
            productCode = "P001",
            unitPrice = 5000.0,
            quantity = 2,
            totalPrice = 10000.0,
            category = Category.FOOD,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        coEvery { receiptItemRepository.findById(receiptItemId) } returns Mono.just(receiptItem)
        coEvery { receiptRepository.findById(receiptId) } returns Mono.just(receipt)

        val input = UpdateReceiptItemRequestDto(
            userId = wrongUserId,
            receiptItemId = receiptItemId,
            productName = "수정된상품",
            productCode = "P999",
            unitPrice = 6000.0,
            quantity = 3,
            category = Category.HEALTH
        )

        // when & then
        assertFailsWith<AccessDeniedException> {
            updateReceiptItemUseCase.execute(input)
        }
    }

    @Test
    fun `receiptItemId가 잘못되면 IllegalArgumentException이 발생한다`() = runTest {
        // given
        val wrongReceiptItemId = 999L

        coEvery { receiptItemRepository.findById(wrongReceiptItemId) } returns Mono.empty()

        val input = UpdateReceiptItemRequestDto(
            userId = 123L,
            receiptItemId = wrongReceiptItemId,
            productName = "수정된상품",
            productCode = "P999",
            unitPrice = 6000.0,
            quantity = 3,
            category = Category.HEALTH
        )

        // when & then
        assertFailsWith<IllegalArgumentException> {
            updateReceiptItemUseCase.execute(input)
        }
    }
}
