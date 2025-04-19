package com.chaw.shopping_note.app.application.usecase.unit

import com.chaw.shopping_note.app.receipt.application.dto.UpdateReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.application.service.ReceiptService
import com.chaw.shopping_note.app.receipt.application.usecase.UpdateReceiptItemUseCase
import com.chaw.shopping_note.app.receipt.domain.Receipt
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import java.time.LocalDateTime

class UpdateReceiptItemUseCaseTest {

    private val receiptItemRepository = mockk<ReceiptItemRepository>()
    private val receiptService = mockk<ReceiptService>(relaxed = true)

    private val updateReceiptItemUseCase = UpdateReceiptItemUseCase(
        receiptItemRepository,
        receiptService
    )

    @Test
    fun success() = runTest {
        // given
        val receipt = createReceipt()
        val receiptItem = createReceiptItem(receipt)
        mockService(receipt, receiptItem)
        mockRepository()
        val input = createTestInput(receipt, receiptItem)

        // when
        val result = updateReceiptItemUseCase.execute(input)

        // then
        assertEquals("수정된상품", result.productName)
        assertEquals("P999", result.productCode)
        assertEquals(6000, result.unitPrice)
        assertEquals(3, result.quantity)
        assertEquals(6000 * 3, result.totalPrice)
        assertEquals(receiptItem.categoryId, result.categoryId)

        coVerify(exactly = 1) { receiptItemRepository.save(receiptItem) }
    }

    private fun createReceipt(): Receipt {
        return Receipt(
            id = 1L,
            userId = 123L,
            storeId = 10L,
            purchaseAt = LocalDateTime.now(),
            createdAt = LocalDateTime.now()
        )
    }

    private fun createReceiptItem(receipt: Receipt): ReceiptItem {
        return ReceiptItem(
            id = 100L,
            receiptId = receipt.id!!,
            productName = "상품1",
            productCode = "P001",
            unitPrice = 5000,
            quantity = 2,
            totalPrice = 10000,
            categoryId = 1L,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    private fun mockService(receipt: Receipt, receiptItem: ReceiptItem) {
        coEvery { receiptService.findReceiptItem(receiptItem.id!!) } returns receiptItem
        coEvery { receiptService.findReceiptWithPermission(receipt.id!!, receipt.userId) } returns receipt
    }

    private fun mockRepository() {
        coEvery { receiptItemRepository.save(any()) } answers { Mono.just(firstArg()) }
    }

    private fun createTestInput(receipt: Receipt, receiptItem: ReceiptItem): UpdateReceiptItemRequestDto {
        return UpdateReceiptItemRequestDto(
            userId = receipt.userId,
            receiptItemId = receiptItem.id!!,
            categoryId = receiptItem.categoryId,
            productName = "수정된상품",
            productCode = "P999",
            unitPrice = 6000,
            quantity = 3,
        )
    }
}
