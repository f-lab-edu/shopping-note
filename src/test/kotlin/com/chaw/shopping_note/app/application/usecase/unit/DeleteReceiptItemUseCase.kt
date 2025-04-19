package com.chaw.shopping_note.app.application.usecase.unit

import com.chaw.shopping_note.app.receipt.application.dto.DeleteReceiptItemRequestDto
import com.chaw.shopping_note.app.receipt.application.service.ReceiptService
import com.chaw.shopping_note.app.receipt.application.usecase.DeleteReceiptItemUseCase
import com.chaw.shopping_note.app.receipt.domain.Receipt
import com.chaw.shopping_note.app.receipt.domain.ReceiptItem
import com.chaw.shopping_note.app.receipt.infrastructure.repository.ReceiptItemRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
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

    @Test
    fun success() = runTest {
        // given
        val receipt = createReceipt()
        val receiptItem = createReceiptItem(receipt)
        mockRepositorySuccess()
        mockServiceFindSuccess(receipt, receiptItem)
        val input = createTestInput(receipt, receiptItem)

        // when
        val result = deleteReceiptItemUseCase.execute(input)

        // then
        assertTrue(result)

        coVerify(exactly = 1) { receiptItemRepository.delete(receiptItem) }
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
            categoryId = 1L,
            productName = "상품1",
            productCode = "P001",
            unitPrice = 5000,
            quantity = 2,
            totalPrice = 10000,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    private fun mockRepositorySuccess() {
        coEvery { receiptItemRepository.delete(any()) } answers { Mono.empty() }
    }

    private fun mockServiceFindSuccess(receipt: Receipt, receiptItem: ReceiptItem) {
        coEvery { receiptService.findReceiptItem(receiptItem.id!!) } returns receiptItem
        coEvery { receiptService.findReceiptWithPermission(receipt.id!!, receipt.userId) } returns receipt
    }

    private fun createTestInput(receipt: Receipt, receiptItem: ReceiptItem): DeleteReceiptItemRequestDto {
        return DeleteReceiptItemRequestDto(
            userId = receipt.userId,
            receiptItemId = receiptItem.id!!
        )
    }
}
