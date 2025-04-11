package com.chaw.shopping_note.app.application.usecase.unit

import com.chaw.shopping_note.app.receipt.application.dto.DeleteReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.application.service.ReceiptService
import com.chaw.shopping_note.app.receipt.application.usecase.DeleteReceiptItemUseCase
import com.chaw.shopping_note.app.receipt.domain.Category
import com.chaw.shopping_note.app.receipt.domain.Receipt
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import java.time.LocalDateTime

class DeleteReceiptItemUseCaseTest {

    private val receiptItemRepository = mockk<ReceiptItemRepository>()
    private val receiptService = mockk<ReceiptService>(relaxed = true)

    private val deleteReceiptItemUseCase = DeleteReceiptItemUseCase(
        receiptItemRepository,
        receiptService
    )

    @BeforeEach
    fun setUp() {
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

        coEvery { receiptService.findReceiptItem(receiptItemId) } returns receiptItem
        coEvery { receiptService.findReceiptWithPermission(receiptId, userId) } returns receipt

        val input = DeleteReceiptItemRequestDto(
            userId = userId,
            receiptItemId = receiptItemId
        )

        // when
        val result = deleteReceiptItemUseCase.execute(input)

        // then
        assertTrue(result)

        coVerify(exactly = 1) { receiptItemRepository.delete(receiptItem) }
        coVerify(exactly = 1) { receiptService.updateReceiptTotal(any()) }
    }
}
