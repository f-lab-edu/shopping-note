package com.chaw.shopping_note.app.application.usecase.unit

import com.chaw.shopping_note.app.receipt.application.dto.UpdateReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.application.usecase.UpdateReceiptItemUseCase
import com.chaw.shopping_note.app.receipt.domain.Category
import com.chaw.shopping_note.app.receipt.domain.Receipt
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*
import org.springframework.security.access.AccessDeniedException

class UpdateReceiptItemUseCaseTest {

    private val receiptRepository = mockk<ReceiptRepository>()
    private val receiptItemRepository = mockk<ReceiptItemRepository>()

    private val updateReceiptItemUseCase = UpdateReceiptItemUseCase(
        receiptRepository,
        receiptItemRepository
    )

    @BeforeEach
    fun setUp() {
        every { receiptRepository.save(any()) } answers { firstArg() }
        every { receiptItemRepository.save(any()) } answers { firstArg() }
    }

    @Test
    fun `영수증 항목을 수정할 수 있다`() {
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

        every { receiptItemRepository.findById(receiptItemId) } returns Optional.of(receiptItem)
        every { receiptRepository.findById(receiptId) } returns Optional.of(receipt)
        every { receiptItemRepository.findAllByReceiptId(receiptId) } returns listOf(receiptItem)

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

        verify(exactly = 1) { receiptItemRepository.save(receiptItem) }
        verify(exactly = 1) { receiptRepository.save(receipt) }
    }

    @Test
    fun `userId가 다르면 AccessDeniedException이 발생한다`() {
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

        every { receiptItemRepository.findById(receiptItemId) } returns Optional.of(receiptItem)
        every { receiptRepository.findById(receiptId) } returns Optional.of(receipt)

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
        assertThrows(AccessDeniedException::class.java) {
            updateReceiptItemUseCase.execute(input)
        }
    }

    @Test
    fun `receiptItemId가 잘못되면 IllegalArgumentException이 발생한다`() {
        // given
        val wrongReceiptItemId = 999L

        every { receiptItemRepository.findById(wrongReceiptItemId) } returns Optional.empty()

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
        assertThrows(IllegalArgumentException::class.java) {
            updateReceiptItemUseCase.execute(input)
        }
    }
}
