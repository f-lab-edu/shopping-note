package com.chaw.shopping_note.app.application.usecase.unit

import com.chaw.shopping_note.app.receipt.application.dto.UpdateReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.application.service.ReceiptService
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

    private val receiptItemRepository = mockk<ReceiptItemRepository>()
    private val receiptService = mockk<ReceiptService>(relaxed = true)

    private val updateReceiptItemUseCase = UpdateReceiptItemUseCase(
        receiptItemRepository,
        receiptService
    )

    @BeforeEach
    fun setUp() {
        coEvery { receiptItemRepository.save(any()) } answers { Mono.just(firstArg()) }
    }

    @Test
    fun `영수증 항목을 수정할 수 있다`() = runTest {
        // given
        val receiptId = 1L
        val receiptItemId = 100L
        val userId = 123L
        val categoryId = 1L

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
            categoryId = categoryId,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        coEvery { receiptService.findReceiptItem(receiptItemId) } returns receiptItem
        coEvery { receiptService.findReceiptWithPermission(receiptId, userId) } returns receipt

        val input = UpdateReceiptItemRequestDto(
            userId = userId,
            receiptItemId = receiptItemId,
            categoryId = categoryId,
            productName = "수정된상품",
            productCode = "P999",
            unitPrice = 6000,
            quantity = 3,
        )

        // when
        val result = updateReceiptItemUseCase.execute(input)

        // then
        assertEquals("수정된상품", result.productName)
        assertEquals("P999", result.productCode)
        assertEquals(6000, result.unitPrice)
        assertEquals(3, result.quantity)
        assertEquals(6000 * 3, result.totalPrice)
        assertEquals(categoryId, result.categoryId)

        coVerify(exactly = 1) { receiptItemRepository.save(receiptItem) }
        coVerify(exactly = 1) { receiptService.updateReceiptTotal(any()) }
    }

}
